package com.psg.adaptive.knowledge_preservation_backend.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.psg.adaptive.knowledge_preservation_backend.entities.RepositoryActivityEntity;
import com.psg.adaptive.knowledge_preservation_backend.entities.RepositoryEntity;
import com.psg.adaptive.knowledge_preservation_backend.enumeration.EnumGithubActivityType;
import com.psg.adaptive.knowledge_preservation_backend.mapper.GithubActivityMapper;
import com.psg.adaptive.knowledge_preservation_backend.repositories.RepositoryActivityRepo;
import com.psg.adaptive.knowledge_preservation_backend.service.GithubRepositoryAgentService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class GithubRepositoryAgentServiceImpl implements GithubRepositoryAgentService {

    private static final Logger log = LoggerFactory.getLogger(GithubRepositoryAgentServiceImpl.class);

    private final RestClient restClient;

    private final ObjectMapper objectMapper;

    private final RepositoryActivityRepo repositoryActivityRepo;

    private final GithubActivityMapper githubActivityMapper;

    @Value("${github.repository.uri}")
    private String githubRepositoryUri;

    @Value("${github.commits.uri}")
    private String githubCommitsUri;

    @Value("${github.issues.uri}")
    private String githubIssuesUri;

    @Value("${github.pulls.uri}")
    private String githubPullsUri;

    @Value("${github.readme.uri}")
    private String githubReadmeUri;

    @Value("${github.branches.uri}")
    private String githubBranchesUri;

    @Value("${github.releases.uri}")
    private String githubReleasesUri;

    public GithubRepositoryAgentServiceImpl(
            RestClient restClient,
            ObjectMapper objectMapper,
            RepositoryActivityRepo repositoryActivityRepo,
            GithubActivityMapper githubActivityMapper
    ) {

        this.restClient = restClient;

        this.objectMapper = objectMapper;

        this.repositoryActivityRepo = repositoryActivityRepo;

        this.githubActivityMapper = githubActivityMapper;
    }

    @Override
    public int collectActivities(RepositoryEntity repository, String accessToken) throws Exception {

        log.info("Starting GitHub activity collection for repository: {}", repository.getFullName());

        if (repository.getFullName() == null || repository.getFullName().isBlank()) {

            throw new IllegalArgumentException("Repository full name is missing");
        }

        int totalCollected = 0;

        /*
         * GitHub expects:
         *
         * /repos/{owner}/{repository}
         *
         * Example:
         *
         * /repos/Nivetha-Selvakumar/my-project
         */

        String repositoryPath = githubRepositoryUri + "/" + repository.getFullName();

        log.info("GitHub repository API path: {}", repositoryPath);

        /*
         * Commits
         */
        totalCollected += collectCommits(repository, repositoryPath, accessToken);

        /*
         * Issues
         */
        totalCollected += collectIssues(repository, repositoryPath, accessToken);

        /*
         * Pull Requests
         */
        totalCollected += collectPullRequests(repository, repositoryPath, accessToken);

        /*
         * README
         */
        totalCollected += collectReadme(repository, repositoryPath, accessToken);

        /*
         * Branches
         */
        totalCollected += collectBranches(repository, repositoryPath, accessToken);

        /*
         * Releases
         */
        totalCollected += collectReleases(repository, repositoryPath, accessToken);

        log.info("GitHub activity collection completed. " + "Repository: {}, Total activities: {}", repository.getFullName(), totalCollected);

        return totalCollected;
    }

    private int collectCommits(RepositoryEntity repository, String repositoryPath, String accessToken) throws Exception {

        log.info("Collecting commits for {}", repository.getFullName());

        JsonNode response = get(repositoryPath + githubCommitsUri, accessToken);

        if (!response.isArray()) {
            log.warn("GitHub commits response is not an array");
            return 0;
        }

        int count = 0;

        for (JsonNode json : response) {

            String externalId = json.path("sha").asText(null);

            if (externalId == null || externalId.isBlank()) {
                continue;
            }

            if (repositoryActivityRepo.findByRepositoryAndActivityTypeAndExternalId(repository,
                    EnumGithubActivityType.COMMIT, externalId).isPresent()) {
                continue;
            }

            RepositoryActivityEntity activity = githubActivityMapper.mapCommit(new RepositoryActivityEntity(), repository, json);

            repositoryActivityRepo.save(activity);

            count++;
        }

        log.info("Commits collected: {}", count);

        return count;
    }

    private int collectIssues(RepositoryEntity repository, String repositoryPath, String accessToken) throws Exception {

        log.info("Collecting issues for {}", repository.getFullName());

        JsonNode response = get(repositoryPath + githubIssuesUri, accessToken, "?state=all");

        if (!response.isArray()) {
            log.warn("GitHub issues response is not an array");
            return 0;
        }

        int count = 0;

        for (JsonNode json : response) {

            /*
             * GitHub's issues API also returns
             * pull requests.
             *
             * We collect pull requests separately.
             */
            if (json.has("pull_request")) {
                continue;
            }

            String externalId =
                    json.path("id").asText(null);

            if (externalId == null || externalId.isBlank()) {
                continue;
            }

            if (repositoryActivityRepo.findByRepositoryAndActivityTypeAndExternalId(repository,
                    EnumGithubActivityType.ISSUE, externalId).isPresent()) {
                continue;
            }

            RepositoryActivityEntity activity = githubActivityMapper.mapIssue(new RepositoryActivityEntity(), repository, json);

            repositoryActivityRepo.save(activity);

            count++;
        }

        log.info("Issues collected: {}", count);

        return count;
    }

    private int collectPullRequests(
            RepositoryEntity repository,
            String repositoryPath,
            String accessToken
    ) throws Exception {

        log.info("Collecting pull requests for {}", repository.getFullName());

        JsonNode response = get(repositoryPath +
                        githubPullsUri,
                accessToken,
                "?state=all"
        );

        if (!response.isArray()) {
            log.warn("GitHub pull requests response is not an array");
            return 0;
        }

        int count = 0;

        for (JsonNode json : response) {

            String externalId = json.path("id").asText(null);

            if (externalId == null || externalId.isBlank()) {
                continue;
            }

            if (repositoryActivityRepo.findByRepositoryAndActivityTypeAndExternalId(repository,
                    EnumGithubActivityType.PULL_REQUEST, externalId).isPresent()) {
                continue;
            }

            RepositoryActivityEntity activity =
                    githubActivityMapper.mapPullRequest(
                            new RepositoryActivityEntity(),
                            repository,
                            json
                    );

            repositoryActivityRepo.save(activity);
            count++;
        }

        log.info("Pull requests collected: {}", count);

        return count;
    }

    private int collectReadme(
            RepositoryEntity repository,
            String repositoryPath,
            String accessToken
    ) throws Exception {

        log.info(
                "Collecting README for {}",
                repository.getFullName()
        );

        try {

            JsonNode json =
                    get(
                            repositoryPath +
                                    githubReadmeUri,
                            accessToken
                    );

            String externalId =
                    json.path("sha").asText(null);

            if (externalId == null ||
                    externalId.isBlank()) {

                log.info(
                        "README not available for {}",
                        repository.getFullName()
                );

                return 0;
            }

            if (repositoryActivityRepo
                    .findByRepositoryAndActivityTypeAndExternalId(
                            repository,
                            EnumGithubActivityType.README,
                            externalId
                    )
                    .isPresent()) {

                log.info(
                        "README already synchronized for {}",
                        repository.getFullName()
                );

                return 0;
            }

            RepositoryActivityEntity activity =
                    githubActivityMapper.mapReadme(
                            new RepositoryActivityEntity(),
                            repository,
                            json
                    );

            repositoryActivityRepo.save(
                    activity
            );

            log.info(
                    "README collected successfully"
            );

            return 1;

        } catch (
                org.springframework.web.client.HttpClientErrorException.NotFound exception
        ) {

            /*
             * README is optional.
             * A 404 means the repository does not
             * currently have a README.
             */

            log.info(
                    "No README found for repository: {}. Skipping README collection.",
                    repository.getFullName()
            );

            return 0;

        } catch (Exception exception) {

            log.error(
                    "Unexpected error while collecting README for {}",
                    repository.getFullName(),
                    exception
            );

            return 0;
        }
    }

    private int collectBranches(
            RepositoryEntity repository,
            String repositoryPath,
            String accessToken
    ) throws Exception {

        log.info(
                "Collecting branches for {}",
                repository.getFullName()
        );

        JsonNode response =
                get(
                        repositoryPath +
                                githubBranchesUri,
                        accessToken
                );

        if (!response.isArray()) {

            log.warn(
                    "GitHub branches response is not an array"
            );

            return 0;
        }

        int count = 0;

        for (JsonNode json : response) {

            String externalId =
                    json.path("name").asText(null);

            if (externalId == null ||
                    externalId.isBlank()) {

                continue;
            }

            if (repositoryActivityRepo
                    .findByRepositoryAndActivityTypeAndExternalId(
                            repository,
                            EnumGithubActivityType.BRANCH,
                            externalId
                    )
                    .isPresent()) {

                continue;
            }

            RepositoryActivityEntity activity =
                    githubActivityMapper.mapBranch(
                            new RepositoryActivityEntity(),
                            repository,
                            json
                    );

            repositoryActivityRepo.save(
                    activity
            );

            count++;
        }

        log.info(
                "Branches collected: {}",
                count
        );

        return count;
    }

    private int collectReleases(
            RepositoryEntity repository,
            String repositoryPath,
            String accessToken
    ) throws Exception {

        log.info(
                "Collecting releases for {}",
                repository.getFullName()
        );

        JsonNode response =
                get(
                        repositoryPath +
                                githubReleasesUri,
                        accessToken
                );

        if (!response.isArray()) {

            log.warn(
                    "GitHub releases response is not an array"
            );

            return 0;
        }

        int count = 0;

        for (JsonNode json : response) {

            String externalId =
                    json.path("id").asText(null);

            if (externalId == null ||
                    externalId.isBlank()) {

                continue;
            }

            if (repositoryActivityRepo
                    .findByRepositoryAndActivityTypeAndExternalId(
                            repository,
                            EnumGithubActivityType.RELEASE,
                            externalId
                    )
                    .isPresent()) {

                continue;
            }

            RepositoryActivityEntity activity =
                    githubActivityMapper.mapRelease(
                            new RepositoryActivityEntity(),
                            repository,
                            json
                    );

            repositoryActivityRepo.save(
                    activity
            );

            count++;
        }

        log.info(
                "Releases collected: {}",
                count
        );

        return count;
    }

    private JsonNode get(
            String uri,
            String accessToken
    ) throws Exception {

        return get(
                uri,
                accessToken,
                ""
        );
    }

    private JsonNode get(
            String uri,
            String accessToken,
            String query
    ) throws Exception {

        log.info(
                "Calling GitHub API: {}{}",
                uri,
                query
        );

        String response =
                restClient
                        .get()
                        .uri(
                                uri + query
                        )
                        .header(
                                "Authorization",
                                "Bearer " +
                                        accessToken
                        )
                        .header(
                                "Accept",
                                "application/vnd.github+json"
                        )
                        .header(
                                "X-GitHub-Api-Version",
                                "2022-11-28"
                        )
                        .accept(
                                MediaType.APPLICATION_JSON
                        )
                        .retrieve()
                        .body(String.class);

        if (response == null ||
                response.isBlank()) {

            throw new IllegalStateException(
                    "Empty response received from GitHub API"
            );
        }

        return objectMapper.readTree(
                response
        );
    }
}