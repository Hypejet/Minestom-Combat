plugins {
    id("java")
}

group = "nl.juriantech"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven { url = uri("https://jitpack.io") }
}

dependencies {
    implementation("net.minestom:minestom-snapshots:1_20_5-95c5f6675f")
}