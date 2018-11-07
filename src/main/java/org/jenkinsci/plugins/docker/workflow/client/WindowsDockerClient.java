package org.jenkinsci.plugins.docker.workflow.client;

import hudson.Launcher;
import hudson.model.Node;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

/**
 * Specialized {@link DockerClient} for windows platform; adjust command lines and other behavior to cover differences
 * between linux and windows systems
 */
class WindowsDockerClient extends DockerClient {

    WindowsDockerClient(@Nonnull Launcher launcher, @CheckForNull Node node, @CheckForNull String toolName) {
        super(launcher, node, toolName);
    }

    @Override
    String defaultVolumeFlags() {
        return ":rw";
    }
}
