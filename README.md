# Cat Picker Library

[![Release](https://jitpack.io/v/msayan/Cat-Picker.svg)](https://jitpack.io/#msayan/Cat-Picker) ![Build](https://github.com/msayan/Cat-Picker/workflows/Build/badge.svg)

## Summary
Create an Android library to pick a cat image from http://thecatapi.com/.
The library shows one screen where the user can scroll through cat pictures. When the user taps on a cat picture the library closes the screen and returns the selected picture. The screen’s action bar shows a back button and a title.
The library should allow customization of the action bar’s color, title, and back button.

## Usage
Create a CatGallery instance
```
gallery = CatGallery.Builder()
            .setActivity(this) // Mandatory field.
            .setTitle("Test")  // Text for toolbar
            .setToolbarColor(R.color.purple_500) // Toolbar color
            .setBackIconDrawable(R.drawable.ic_baseline_chevron_left) // Back icon drawable
            .setImageListener { } // Callback for selected image
            .build()
```
To show Cat gallery you should call `show` function
```
gallery.show()
```

You must override `onActivityResult` on your activity to make callback work
```
override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        gallery.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }
```

## Download

### Step 1. Add the JitPack repository to your build file

Add it in your root build.gradle at the end of repositories:

```groovy

	allprojects {
		repositories {
			maven { url 'https://jitpack.io' }
		}
	}
```

### Step 2. Add the dependency

```groovy
    dependencies {
	    implementation 'com.github.msayan:Cat-Picker:$last_version'
	    }
```