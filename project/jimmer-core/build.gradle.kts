plugins {
    `java-library`
    kotlin("jvm") version "1.6.10"
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
    withSourcesJar()
    withJavadocJar()
}

repositories {
    mavenCentral()
}

dependencies {

    implementation("javax.validation:validation-api:2.0.1.Final")
    api("com.fasterxml.jackson.core:jackson-databind:2.15.2")
    api("org.jetbrains.kotlin:kotlin-reflect:1.7.10")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.15.2")
    implementation("org.apache.commons:commons-lang3:3.12.0")
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.7.10")
    compileOnly("org.mapstruct:mapstruct:1.5.3.Final")

    testImplementation("javax.validation:validation-api:2.0.1.Final")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
    testImplementation("org.mapstruct:mapstruct:1.5.3.Final")
    testImplementation("org.projectlombok:lombok:1.18.30")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")

    testAnnotationProcessor(project(":jimmer-apt"))
    testAnnotationProcessor("org.projectlombok:lombok:1.18.30")
    testAnnotationProcessor("org.mapstruct:mapstruct-processor:1.5.3.Final")
}

tasks.withType(JavaCompile::class) {
    options.compilerArgs.add("-parameters")
    options.compilerArgs.add("-Ajimmer.source.excludes=org.babyfish.jimmer.invalid")
    options.compilerArgs.add("-Ajimmer.generate.dynamic.pojo=true")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

tasks.withType<Javadoc>{
    options.encoding = "UTF-8"
}