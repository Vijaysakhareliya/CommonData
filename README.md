# Here is Common Function which we used in regular coding


## build.gradle Project level
Add this code

```
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}
```

## build.gradle App level
Add this code
```
dependencies {
    implementation 'com.github.Vijaysakhareliya:CommonData:<latest_version>'
}
```
