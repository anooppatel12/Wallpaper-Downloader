# Wallpaper Downloader App

The **Wallpaper Downloader App** is an Android application that allows users to browse, download, and set wallpapers as their home screen background. Users can choose from a variety of wallpapers, preview them, and download them for offline use. Additionally, they can directly set any wallpaper as their device's background.

## Features

- **Browse Wallpapers**: A wide range of wallpapers in different categories and resolutions.
- **Download Wallpapers**: Users can download wallpapers directly to their device.
- **Set Wallpapers**: Wallpapers can be set directly as the device's home or lock screen background.
- **High-Quality Images**: Wallpapers are displayed in high-resolution to ensure clarity on different screen sizes.


## Technologies Used

- **Language**: Java
- **Image Loading**: Glide
- **UI Components**: RecyclerView, CardView, Buttons, ImageView
- **Wallpaper Management**: WallpaperManager
- **Storage**: Local storage for downloaded wallpapers
- **Firebase**: using Firebase for hosting wallpaper images



### Wallpaper Display

- Wallpapers are displayed in a **RecyclerView** in a grid format.
- Each item contains an image preview and two buttons:
  - **Download Button**: Downloads the wallpaper to local storage.
  - **Set Wallpaper Button**: Directly sets the wallpaper as the home screen or lock screen background.

### Wallpaper Download and Set

- **Glide** is used to load wallpaper images efficiently, ensuring smooth performance.
- **WallpaperManager** is used to set the downloaded wallpaper as the device's home screen or lock screen background.

## Requirements

- Android Studio 4.0+
- Java 8+
- Minimum Android SDK 21 (Android 5.0 Lollipop)

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.


## Contact

- **Author**: Anoop Patel
- **GitHub**: (https://github.com/anooppatel12)
- **Email**: anooppbh8@gmail.com
