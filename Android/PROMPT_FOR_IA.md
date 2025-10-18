
# AI Prompt for Virtues Library

You are an expert Android developer AI. Your task is to understand and effectively use the Virtues library, a sophisticated dimension management system for Android Views and Jetpack Compose. This guide will provide you with all the necessary information about the library's architecture, functionalities, and use cases.

## 1. Installation / Setup

Github: [Virtues Repository](https://github.com/Bodenberg/Virtues)

Since this is a multi-module project, you will need to add the Virtues modules as project dependencies in your `build.gradle.kts` (or `build.gradle`) file.

```kotlin
dependencies {
    // Core (Dynamic + Fixed)
    implementation("com.github.bodenberg.virtues:virtues-dynamic:1.0.0")

    // SDP & SSP scaling (optional)
    implementation("com.github.bodenberg.virtues:virtues-sdps:1.0.0")
    implementation("com.github.bodenberg.virtues:virtues-ssps:1.0.0")

    // All in one
    implementation("com.github.bodenberg.virtues:virtues-all:1.0.0")
}

maven { url 'https://jitpack.io' } //or maven central
```

```kotlin
dependencies {
    // Core (Dynamic + Fixed)
    implementation("ag.virtues.dimens:virtues-dynamic:1.0.0")

    // SDP & SSP scaling (optional)
    implementation("ag.virtues.dimens:virtues-sdps:1.0.0")
    implementation("ag.virtues.dimens:virtues-ssps:1.0.0")

    // All in one
    implementation("ag.virtues.dimens:virtues-all:1.0.0")
}

mavenCentral()
```

## 2. Library Initialization

Virtues does not require any manual initialization. The library is designed to work out of the box without any setup in your `Application` class.

## 3. Core Concepts

### 3.1. What is Virtues?

Virtues is a library that provides a responsive and mathematically refined way to manage dimensions (Dp, Sp, Px) in Android applications. It ensures layout consistency across various screen sizes and ratios by treating standard Dp values as a base that is dynamically scaled.

### 3.2. Key Modules

The Virtues library is structured into the following key modules:

-   **`virtues_library`**: The base module containing fundamental classes and enums like `ScreenType` and `DpQualifier`.
-   **`virtues_dynamic`**: The core module that implements the two main scaling models: **Fixed (FX)** and **Dynamic (DY)**. It provides separate implementations for both Android Views (`/code`) and Jetpack Compose (`/compose`).
-   **`virtues_sdps`**: A module that offers a traditional SDP (Scalable DP) approach, allowing you to define dimensions that scale based on screen width, height, or smallest width. It also supports custom dimension values for different UI modes (e.g., TV, Watch).
-   **`virtues_ssps`**: Similar to `virtues_sdps`, but for SSP (Scalable SP), focusing on text size scaling.
-   **`virtues_all`**: A module that likely combines all the `virtues` functionalities into a single dependency.
-   **`app`**: A sample application module that demonstrates how to use the Virtues library in a real-world scenario.

### 3.3. Scaling Models: Fixed (FX) vs. Dynamic (DY)

The library's core functionality revolves around two primary scaling models:

| Feature | Fixed (FX) | Dynamic (DY) |
| --- | --- | --- |
| **Philosophy** | **Logarithmic Adjustment**: Subtle, refined scaling that decelerates on very large screens. Recommended for most UI components. | **Proportional Adjustment**: Aggressive, percentage-based scaling that maintains a constant proportion to the screen size. |
| **Use Case** | Ideal for components where visual comfort is key, such as button heights, paddings, icons, and title fonts. | Best suited for layout elements that need to fill a consistent fraction of the available space, like the width of cards in a grid. |
| **Implementation** | Available in `virtues_dynamic` for both [Views](virtues_dynamic/src/main/java/com/virtues/dynamic/code/Virtues.kt) and [Compose](virtues_dynamic/src/main/java/com/virtues/dynamic/compose/Virtues.kt). | Available in `virtues_dynamic` for both [Views](virtues_dynamic/src/main/java/com/virtues/dynamic/code/Virtues.kt) and [Compose](virtues_dynamic/src/main/java/com/virtues/dynamic/compose/Virtues.kt). |

## 4. How to Use Virtues

### 4.1. Jetpack Compose

In Jetpack Compose, Virtues is primarily used through a set of convenient extension functions on `Int`, `Float`, `Dp`, and `TextUnit`.

#### 4.1.1. `virtues_dynamic`

The `virtues_dynamic` module provides a rich set of extensions for both **Fixed** and **Dynamic** scaling.

**Fixed Scaling (FX):**

-   Use the `.fxdp`, `.fxsp`, `.fxem`, and `.fxpx` extensions for subtle, logarithmically-scaled dimensions.
-   **Example**: `16.dp.fxdp` will provide a slightly larger Dp value on a tablet compared to a phone.

**Dynamic Scaling (DY):**

-   Use the `.dydp`, `.dysp`, `.dyem`, and `.dypx` extensions for proportional, percentage-based scaling.
-   **Example**: `100.dp.dydp` will scale the dimension to maintain the same percentage of the screen width.

**Percentage-Based Dimensions:**

You can also create dimensions based on a percentage of the screen size:

-   `Virtues.dynamicPercentageDp(0.5f)` will return a Dp value equivalent to 50% of the screen's smaller dimension.

#### 4.1.2. `virtues_sdps` and `virtues_ssps`

These modules provide a more traditional, resource-based scaling approach.

-   **SDP**: Use the `.sdp`, `.hdp`, and `.wdp` extensions to scale dimensions based on the smallest width, height, or width of the screen, respectively.
-   **SSP**: Use the `.ssp`, `.hsp`, and `.wsp` extensions for scalable text sizes. You can also use `.sem`, `.hem`, and `.wem` for non-font-scaled text.
-   **Custom Scaling**: Both modules offer a `scaledDp()` and `scaledSp()` builder to define custom dimensions for different screen configurations (e.g., for `UiModeType.TELEVISION`).

### 4.2. Android Views (XML & Kotlin)

For traditional Android Views, Virtues provides a `Virtues` singleton object that acts as a gateway to the scaling functions.

**`virtues_dynamic`:**

-   **Fixed Scaling**: `Virtues.fixed(16f).dp`
-   **Dynamic Scaling**: `Virtues.dynamic(100f).dp`
-   **Data Binding**: The `app` module demonstrates the use of `DimensBindingAdapters` to apply Virtues directly in XML layouts.

## 5. Data Binding Usage

To use Virtues with Data Binding, you can create custom `BindingAdapter` functions. The `app` module provides a sample implementation in `DimensBindingAdapters.kt`.

```xml
<layout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto">

    <data>
        <import type="com.virtues.dynamic.code.Virtues"/>
    </data>

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:orientation="vertical"
        app:dynamicHeightDp="@{100f}"
        app:dynamicWidthDp="@{100f}">

        <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="Title with Dynamic Font"
            app:dynamicTextSizeDp="@{20f}"/>

    </LinearLayout>
</layout>
```
## 6. Advanced Features

### 6.1. `DpQualifier` and `UiModeType`

The library allows for fine-grained control over dimension scaling:

-   **`DpQualifier`**: An enum to specify whether the scaling should be based on `SMALL_WIDTH`, `WIDTH`, or `HEIGHT`.
-   **`UiModeType`**: An enum to apply different scaling factors for specific device types, such as `TELEVISION`, `CAR`, or `WATCH`.

### 6.2. Physical Units (`VirtuesPhysicalUnits`)

The `virtues_dynamic` module includes a utility, `VirtuesPhysicalUnits`, for converting physical units (mm, cm, inch) to pixels. This is particularly useful for applications that require real-world precision, such as on wearables.

### 6.3. Layout Utilities (`calculateAvailableItemCount`)

Virtues provides a `calculateAvailableItemCount` function to dynamically determine the number of items that can fit within a container. This is extremely useful for calculating the `spanCount` in a `GridLayoutManager` or `LazyVerticalGrid`.

## 7. When to Use Each Module

-   **`virtues_dynamic`**: The recommended default for most use cases. It offers a powerful and flexible way to manage dimensions with its **Fixed** and **Dynamic** scaling models.
-   **`virtues_sdps` / `virtues_ssps`**: Use these when you prefer a more traditional, resource-based scaling approach, or when you need to define specific dimension values for different device types (e.g., a larger font on TVs).
-   **`virtues_all`**: Use this if you want to include all the functionalities of the Virtues library with a single dependency.

## 8. Performance Considerations

Virtues is optimized for high performance. Dimension values are calculated once and cached to prevent recalculation overhead during screen redraws (recompositions or layout passes). The impact on app startup time is minimal.

## 9. Guide to Testability

For UI (instrumentation) tests, Virtues will function normally, adapting to the screen dimensions of the test device or emulator. For unit tests, where a real `Context` is unavailable, you can mock the `Virtues` class or its extension functions to return deterministic values, ensuring your tests are predictable.

By following this guide, you should be able to effectively use the Virtues library to create responsive and visually consistent Android applications. Remember to analyze the context of the user's request to determine the most appropriate scaling model and module to use.
