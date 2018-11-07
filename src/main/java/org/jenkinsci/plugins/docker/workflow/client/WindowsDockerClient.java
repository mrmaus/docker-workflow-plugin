package org.jenkinsci.plugins.docker.workflow.client;

import hudson.EnvVars;
import hudson.Launcher;
import hudson.model.Node;
import hudson.util.ArgumentListBuilder;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Map;

/**
 * Specialized {@link DockerClient} for windows platform; adjust command lines and other behavior to cover differences
 * between linux and windows systems
 */
class WindowsDockerClient extends DockerClient {

    WindowsDockerClient(@Nonnull Launcher launcher, @CheckForNull Node node, @CheckForNull String toolName) {
        super(launcher, node, toolName);
    }

    @Override
    ArgumentListBuilder runArguments(@Nonnull String image,
                                     @CheckForNull String args,
                                     @CheckForNull String workdir,
                                     @Nonnull Map<String, String> volumes,
                                     @Nonnull Collection<String> volumesFromContainers,
                                     @Nonnull EnvVars containerEnv,
                                     @Nonnull String command) {
        ArgumentListBuilder argb = new ArgumentListBuilder();

        argb.add("run", "-t", "-d");
        if (args != null) {
            argb.addTokenized(args);
        }

        if (workdir != null) {
            argb.add("-w", workdir);
        }
        for (Map.Entry<String, String> volume : volumes.entrySet()) {
            argb.add("-v", volume.getKey() + ":" + volume.getValue() + ":rw");
        }
        for (String containerId : volumesFromContainers) {
            argb.add("--volumes-from", containerId);
        }
        for (Map.Entry<String, String> variable : containerEnv.entrySet()) {
            argb.add("-e");
            argb.addMasked(variable.getKey() + "=" + variable.getValue());
        }
        argb.add("--entrypoint").add(command).add(image);
        return argb;
    }
}
