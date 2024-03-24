plugins {
    kotlin("jvm") version "1.7.10"
    id("com.google.devtools.ksp") version "1.7.10-1.0.6"
    id("org.jetbrains.dokka") version "1.6.10"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(kotlin("reflect"))
    api(project(":jimmer-core-kotlin"))
    api(project(":jimmer-sql"))
    implementation("org.apache.commons:commons-lang3:3.12.0")

    testImplementation(kotlin("test"))
    kspTest(project(":jimmer-ksp"))
    testAnnotationProcessor(project(":jimmer-ksp"))

    testImplementation("com.h2database:h2:2.1.212")
    testImplementation("javax.validation:validation-api:2.0.1.Final")
    dokkaHtmlPlugin("org.jetbrains.dokka:dokka-base:1.6.0")
    testImplementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.15.2")
    testImplementation("org.postgresql:postgresql:42.3.6")
    testImplementation("org.springframework:spring-jdbc:5.3.20")
    testImplementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.15.2")
}

kotlin {
    sourceSets.test {
        kotlin.srcDir("build/generated/ksp/test/kotlin")
    }
}

java.sourceCompatibility = JavaVersion.VERSION_1_8
java.targetCompatibility = JavaVersion.VERSION_1_8

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "1.8"
    }
}

java {
    withSourcesJar()
    withJavadocJar()
}

ksp {
    arg("jimmer.dto.mutable", "true")
}

tasks {
    withType(Jar::class) {
        if (archiveClassifier.get() == "javadoc") {
            dependsOn(dokkaHtml)
            from("build/dokka/html")
        }
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "1.8"
    }
}

tasks.withType<Javadoc>{
    options.encoding = "UTF-8"
}