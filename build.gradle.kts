plugins {
    java
    application
    id("org.openjfx.javafxplugin") version "0.0.13"
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("org.beryx.jlink") version "2.26.0"
}

repositories {
    mavenCentral()
}

dependencies {
    // No extra dependencies are needed for JavaFX itself

    implementation("org.openjfx:javafx-controls:17.0.2")

    testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")
    testImplementation("org.assertj:assertj-core:3.24.2")
    testImplementation("com.ocadotechnology.gembus:test-arranger:1.6.4")
}

tasks.test {
    useJUnitPlatform()
}

javafx {
    version = "17"
    modules = listOf("javafx.controls")
}

application {
    mainClass.set("pl.marianjureczko.testcsv.Main")
    mainModule.set("pl.marianjureczko.testcsv")
}

tasks.shadowJar {
    archiveBaseName.set("Testy")
    archiveClassifier.set("")
    archiveVersion.set("1.0")
}

jlink {
    imageName.set("Testy")
    options.addAll("--add-modules", "javafx.controls")

    jpackage {
        installerType = "exe" // Only valid on Windows
        installerOptions.addAll(
                listOf(
                        "--win-shortcut", // creates a Start Menu shortcut
                        "--win-menu",     // adds to Windows menu
                        "--win-dir-chooser", // allows install location selection
                        "--win-per-user-install" // user-level install
                )
        )
    }
}