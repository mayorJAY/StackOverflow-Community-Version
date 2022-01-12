<p align="center">
  <img src="app_icon.png" title="App Logo">
</p>

# StackOverflow - Community Version

An Android StackOverflow client application implemented using the MVVM pattern, Retrofit2, LiveData, ViewModel, Repository pattern, View Binding. Users can get to view Questions which have been asked on Stack Overflow; picking a particular Question makes the user view it in details as well as the Answers provided. These Questions can be filtered by any of these four categories; Active, Recent, Hot or Voted. Questions that have an accepted Answer are easily identified.
Users can also search for a particular problem they are having by typing in any search query of choice. Questions are curated based on the search query and presented to the user; again, the user can pick a particular Question to view the provided Answers.
# Tech Stack

* [Retrofit](https://square.github.io/retrofit/) which is a type-safe REST client for Android which makes it easier to consume RESTful web services
* [Paging Library](https://developer.android.com/topic/libraries/architecture/paging) which helps to load and display small chunks of data at a time
* [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel) used to store and manage UI-related data in a lifecycle conscious way
* [LiveData](https://developer.android.com/topic/libraries/architecture/livedata) which is an observable data holder class used to handle data in a lifecycle-aware manner
* [Glide](https://github.com/bumptech/glide) which is an image loading and caching library for Android
* [Material Design](https://material.io/develop/android/docs/getting-started/) which is an adaptable system that guides in maintaining principles and best practices of contemporary UI
* [View Binding](https://developer.android.com/topic/libraries/view-binding) used to easily write code that interacts with views by referencing them directly
* [Espresso](https://developer.android.com/training/testing/espresso) used to write concise, beautiful, and reliable Android UI tests
* [MarkdownView](https://github.com/mukeshsolanki/MarkdownView-Android) which is a simple library that helps to display Markdown text or files on Android as a HTML page just like GitHub
* [ML Kit OCR](https://developers.google.com/ml-kit/vision/text-recognition) which is a library that recognizes text in any captured image
* [Android Image Cropper](https://github.com/ArthurHub/Android-Image-Cropper) which is an image cropping library for Android, optimized for camera and gallery.
* [Dagger](https://developer.android.com/training/dependency-injection/dagger-android) for DI
# Installation

FlowOver Stack requires a minimum API level of 21. Clone the repository. You will need an API key from [Stack Exchange API](https://api.stackexchange.com/) to receive a higher request quota. Locate the StringConstants.java file and edit the following line to add your API key:

````
API_KEY = "YOUR_API_KEY"
````

# Contribution
All contributions are welcome. See the [CONTRIBUTING](https://github.com/mayorJAY/FlowOverStack/blob/feat/ocr/CONTRIBUTING.md) file for guidelines on contributing

<a href='https://play.google.com/store/apps/details?id=com.josycom.mayorjay.flowoverstack&pcampaignid=pcampaignidMKT-Other-global-all-co-prtnr-py-PartBadge-Mar2515-1'><img alt='Get it on Google Play' src='https://play.google.com/intl/en_gb/badges/static/images/badges/en_badge_web_generic.png' width="280"/></a>

## LICENSE
```
MIT License
Copyright (c) 2021 Joseph Olugbohunmi

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```
