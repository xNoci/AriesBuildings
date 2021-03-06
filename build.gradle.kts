import com.github.jengelman.gradle.plugins.shadow.tasks.ConfigureShadowRelocation
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.apache.tools.ant.filters.ReplaceTokens

group "net.tttproject"
version = project.property("version")!!

plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

repositories {
    mavenCentral()
    mavenLocal()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.dmulloy2.net/repository/public/")
    maven {
        url = uri("http://23.88.43.243:8081/repository/tttproject/")
        isAllowInsecureProtocol = true

        credentials {
            username = "dev"
            password = "wGgQ/;GN7qeyK+F!"
        }
    }
}

dependencies {
    implementation("xyz.upperlevel.spigot.book:spigot-book-api:1.6")

    implementation(platform("com.intellectualsites.bom:bom-1.18.x:1.9"))
    compileOnly("com.fastasyncworldedit:FastAsyncWorldEdit-Core")
    compileOnly("com.fastasyncworldedit:FastAsyncWorldEdit-Bukkit")

    compileOnly("com.comphenix.protocol:ProtocolLib:4.7.0")
    compileOnly("net.luckperms:api:5.4")
    compileOnly("org.spigotmc:spigot:1.16.5-R0.1-SNAPSHOT")
    compileOnly("net.tttproject:project-system:1.3.1")

    compileOnly("org.projectlombok:lombok:1.18.24")
    annotationProcessor("org.projectlombok:lombok:1.18.24")
}

the<JavaPluginExtension>().toolchain {
    languageVersion.set(JavaLanguageVersion.of(17))
}


task<ConfigureShadowRelocation>("relocateShadowJar") {
    target = tasks["shadowJar"] as ShadowJar
    prefix = "net.tttproject.dep"
}

tasks.named<ShadowJar>("shadowJar").configure {
    dependsOn(tasks["relocateShadowJar"])
}

tasks {
    shadowJar {
        archiveFileName.set("${project.property("name")}-${project.property("version")}.jar")

    }

    compileJava {
        options.encoding = Charsets.UTF_8.name()
    }

    processResources {
        filter<ReplaceTokens>(
            "beginToken" to "\${",
            "endToken" to "}",
            "tokens" to mapOf(
                "plugin.version" to project.property("version"),
                "plugin.name" to project.property("name")
            )
        )
    }
}

