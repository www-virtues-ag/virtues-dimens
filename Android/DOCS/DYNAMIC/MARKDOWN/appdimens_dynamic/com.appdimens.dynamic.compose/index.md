//[virtues_dynamic](../../index.md)/[com.virtues.dynamic.compose](index.md)

# Package-level declarations

## Types

| Name | Summary |
|---|---|
| [Virtues](-app-dimens/index.md) | [androidJvm]<br>@[Stable](https://developer.android.com/reference/kotlin/androidx/compose/runtime/Stable.html)<br>object [Virtues](-app-dimens/index.md)<br>EN Singleton object that provides functions for responsive dimension management in Jetpack Compose, acting as a gateway to the Fixed and Dynamic builders. |
| [VirtuesAdjustmentFactors](-app-dimens-adjustment-factors/index.md) | [androidJvm]<br>@[Stable](https://developer.android.com/reference/kotlin/androidx/compose/runtime/Stable.html)<br>object [VirtuesAdjustmentFactors](-app-dimens-adjustment-factors/index.md)<br>EN Singleton object that provides functions for calculating and resolving adjustment factors and screen qualifiers. |
| [VirtuesDynamic](-app-dimens-dynamic/index.md) | [androidJvm]<br>class [VirtuesDynamic](-app-dimens-dynamic/index.md)(initialBaseDp: [Dp](https://developer.android.com/reference/kotlin/androidx/compose/ui/unit/Dp.html), ignoreMultiViewAdjustment: [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html) = false)<br>EN A builder class for creating dynamic dimensions that allow base Dp customization via screen qualifiers (`.screen()`). The final value is scaled by the screen size. |
| [VirtuesFixed](-app-dimens-fixed/index.md) | [androidJvm]<br>class [VirtuesFixed](-app-dimens-fixed/index.md)(initialBaseDp: [Dp](https://developer.android.com/reference/kotlin/androidx/compose/ui/unit/Dp.html), ignoreMultiViewAdjustment: [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html) = false)<br>EN A builder class for creating &quot;fixed&quot; dimensions that are automatically adjusted based on the device's `smallestScreenWidthDp` and screen aspect ratio. |
| [VirtuesPhysicalUnits](-app-dimens-physical-units/index.md) | [androidJvm]<br>object [VirtuesPhysicalUnits](-app-dimens-physical-units/index.md)<br>EN Singleton object providing functions for physical unit conversion (MM, CM, Inch) and measurement utilities. |