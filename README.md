# Wallpaper App

This is a wallpaper application built with Kotlin and Android Studio.

## Prerequisites

- Android Studio Iguana | 2023.2.1 Patch 1 or later
- JDK 8 or later
- Android SDK

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Cloning the Project

1. Open Android Studio.
2. Go to `File > New > Project from Version Control`.
3. In the URL field, enter the Git repository URL. For example: `https://github.com/yeshwanthmunisifreddy/wallpaper.git`.
4. Click `Clone`.

### Setting Up the Project

1. After cloning the project, Android Studio will automatically try to import the project.
2. If your Android Studio does not automatically import the project, go to `File > Open` and select the project directory.
3. Wait for Android Studio to import the project and download any necessary Gradle dependencies.
4. Once the project has been imported, you should be able to run the app by clicking `Run > Run 'app'`.


## Using AsyncImage

`AsyncImage` is a utility that allows you to load images asynchronously in your Compose application. Here's a simple example of how to use it:

```kotlin
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.google.accompanist.imageloading.AsyncImage

@Composable
fun ImagePreview() {
    AsyncImage(
        model = "https://example.com/image.jpg",
        contentDescription = "Example Image",
        modifier = Modifier.size(100.dp)
    )
}



import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.google.accompanist.imageloading.AsyncImage

@Composable
fun ImagePreview() {
    AsyncImage(
        model = "https://example.com/image.jpg",
        contentDescription = "Example Image",
        modifier = Modifier.size(100.dp),
        placeHolder = {/* Your placeholder Composable here */
           Box(
                modifier = Modifier
                   .background(color = color, shape = RoundedCornerShape(8.dp))
                   .aspectRatio(aspectRatio)
                   .fillMaxSize()
          )
       },
       error = {/* Your error handling Composable here */}
    )
}
