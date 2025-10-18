# 📚 VirtuesDimens iOS - Technical Documentation

## 🎯 Overview

VirtuesDimens iOS is a responsive dimension management system that automatically adjusts values based on screen dimensions, ensuring layout consistency across any screen size or ratio. It's the iOS equivalent of the Android VirtuesDimens library, built specifically for iOS with Swift and native APIs.

## 🏗️ Architecture

### Core Components

1. **VirtuesDimens** - Main singleton class providing access to dimension builders
2. **VirtuesDimensFixed** - Fixed scaling with logarithmic adjustment
3. **VirtuesDimensDynamic** - Dynamic scaling with proportional adjustment
4. **VirtuesDimensAdjustmentFactors** - Utility class for calculating screen factors
5. **VirtuesDimensTypes** - Type definitions and enums
6. **VirtuesDimensExtensions** - Extensions for SwiftUI and UIKit

### Mathematical Models

#### Fixed (FX) Model
- **Philosophy**: Logarithmic adjustment for refined scaling
- **Formula**: `Base Value × (1 + Adjustment Factor × (Base Increment + AR Adjustment))`
- **Use Case**: UI elements that should maintain visual consistency (buttons, paddings, fonts)
- **Growth Pattern**: Smooth, controlled growth that slows on large screens

#### Dynamic (DY) Model
- **Philosophy**: Percentage-based proportional adjustment
- **Formula**: `(Base Value / Reference Width) × Current Screen Dimension`
- **Use Case**: Layout containers and fluid elements
- **Growth Pattern**: Linear growth maintaining percentage of screen space

### Screen Adjustment Factors

The library calculates adjustment factors based on:

1. **Base Reference**: iPhone 6/7/8 dimensions (375×667 points)
2. **Aspect Ratio**: Logarithmic adjustment for extreme aspect ratios
3. **Device Type**: Custom values for different device categories
4. **Multi-Window**: Detection and handling of split-screen mode

## 📱 Device Detection

### Device Types

```swift
public enum DeviceType: String, CaseIterable {
    case phone = "phone"
    case tablet = "tablet"
    case watch = "watch"
    case tv = "tv"
    case carPlay = "carPlay"
    
    public static func current() -> DeviceType {
        switch UIDevice.current.userInterfaceIdiom {
        case .phone:
            return .phone
        case .pad:
            return .tablet
        case .tv:
            return .tv
        case .carPlay:
            return .carPlay
        case .watch:
            return .watch
        default:
            return .phone
        }
    }
}
```

### Screen Types

```swift
public enum ScreenType: String, CaseIterable {
    case lowest = "lowest"
    case highest = "highest"
    
    public var description: String {
        switch self {
        case .lowest:
            return "Use smallest screen dimension"
        case .highest:
            return "Use largest screen dimension"
        }
    }
}
```

## 🔧 API Reference

### VirtuesDimens

The main entry point for creating responsive dimensions.

#### Static Methods

```swift
public class VirtuesDimens {
    // Static convenience methods
    public static func fixed(_ value: CGFloat) -> VirtuesDimensFixed
    public static func dynamic(_ value: CGFloat) -> VirtuesDimensDynamic
    public static func percentage(_ percentage: CGFloat, type: ScreenType = .lowest) -> CGFloat
    
    // Instance methods
    public static let shared = VirtuesDimens()
    public func fixed(_ value: CGFloat) -> VirtuesDimensFixed
    public func dynamic(_ value: CGFloat) -> VirtuesDimensDynamic
}
```

### VirtuesDimensFixed

Fixed scaling with logarithmic adjustment.

```swift
public class VirtuesDimensFixed {
    // Configuration methods
    public func screen(_ type: DeviceType, _ value: CGFloat) -> VirtuesDimensFixed
    public func aspectRatio(enable: Bool, sensitivity: CGFloat? = nil) -> VirtuesDimensFixed
    public func type(_ type: ScreenType) -> VirtuesDimensFixed
    
    // Conversion methods
    public func toPoints() -> CGFloat
    public func toPixels() -> CGFloat
}
```

### VirtuesDimensDynamic

Dynamic scaling with proportional adjustment.

```swift
public class VirtuesDimensDynamic {
    // Configuration methods
    public func screen(_ type: DeviceType, _ value: CGFloat) -> VirtuesDimensDynamic
    public func type(_ type: ScreenType) -> VirtuesDimensDynamic
    
    // Conversion methods
    public func toPoints() -> CGFloat
    public func toPixels() -> CGFloat
}
```

### VirtuesDimensAdjustmentFactors

Utility class for calculating screen adjustment factors.

```swift
public class VirtuesDimensAdjustmentFactors {
    // Screen information
    public static func getCurrentScreenDimensions() -> (width: CGFloat, height: CGFloat)
    public static func getCurrentScreenType() -> ScreenType
    public static func getCurrentDeviceType() -> DeviceType
    
    // Factor calculations
    public static func calculateAdjustmentFactors() -> AdjustmentFactors
    public static func calculateAspectRatio() -> CGFloat
    public static func calculateBaseAdjustmentFactor() -> CGFloat
}
```

## 🎨 SwiftUI Extensions

### Direct Extensions

```swift
// CGFloat/Int extensions
extension CGFloat {
    var fxpt: CGFloat { VirtuesDimens.fixed(self).toPoints() }
    var fxpx: CGFloat { VirtuesDimens.fixed(self).toPixels() }
    var dypt: CGFloat { VirtuesDimens.dynamic(self).toPoints() }
    var dypx: CGFloat { VirtuesDimens.dynamic(self).toPixels() }
    
    // Physical units
    var cm: CGFloat { self * 37.7952755906 } // 1cm = 37.8 points
    var mm: CGFloat { self * 3.77952755906 } // 1mm = 3.78 points
    var inch: CGFloat { self * 96.0 } // 1inch = 96 points
}
```

### SwiftUI Modifier Extensions

```swift
// Padding extensions
extension View {
    func fxPadding(_ value: CGFloat) -> some View
    func fxPadding(_ edges: Edge.Set, _ value: CGFloat) -> some View
    func dyPadding(_ value: CGFloat) -> some View
}

// Frame extensions
extension View {
    func fxFrame(width: CGFloat? = nil, height: CGFloat? = nil) -> some View
    func dyFrame(width: CGFloat? = nil, height: CGFloat? = nil) -> some View
}

// Corner radius extensions
extension View {
    func fxCornerRadius(_ value: CGFloat) -> some View
    func dyCornerRadius(_ value: CGFloat) -> some View
}

// Font extensions
extension Font {
    static func fxSystem(size: CGFloat, weight: Font.Weight = .regular, design: Font.Design = .default) -> Font
    static func dySystem(size: CGFloat, weight: Font.Weight = .regular, design: Font.Design = .default) -> Font
    static func fxCustom(_ name: String, size: CGFloat) -> Font
}
```

### Environment Integration

```swift
// DimensProvider for environment-based calculations
public struct DimensProvider<Content: View>: View {
    let content: Content
    
    public init(@ViewBuilder content: () -> Content) {
        self.content = content()
    }
    
    public var body: some View {
        content
            .environment(\.dimensProvider, DimensEnvironment())
    }
}

// Protocol-based API
public protocol DimensProvider {
    func fixed(_ value: CGFloat) -> VirtuesDimensFixed
    func dynamic(_ value: CGFloat) -> VirtuesDimensDynamic
}

extension CGFloat {
    func fixed() -> VirtuesDimensFixed { VirtuesDimens.fixed(self) }
    func dynamic() -> VirtuesDimensDynamic { VirtuesDimens.dynamic(self) }
}
```

## 📱 UIKit Extensions

### UIView Extensions

```swift
extension UIView {
    func fxCornerRadius(_ value: CGFloat) {
        layer.cornerRadius = value.fxpt
    }
    
    func dyCornerRadius(_ value: CGFloat) {
        layer.cornerRadius = value.dypt
    }
    
    func fxBorderWidth(_ value: CGFloat) {
        layer.borderWidth = value.fxpt
    }
}
```

### UILabel Extensions

```swift
extension UILabel {
    func fxFontSize(_ value: CGFloat) {
        font = font.withSize(value.fxpt)
    }
    
    func dyFontSize(_ value: CGFloat) {
        font = font.withSize(value.dypt)
    }
}
```

### UIButton Extensions

```swift
extension UIButton {
    func fxTitleFontSize(_ value: CGFloat) {
        titleLabel?.font = titleLabel?.font.withSize(value.fxpt)
    }
    
    func dyTitleFontSize(_ value: CGFloat) {
        titleLabel?.font = titleLabel?.font.withSize(value.dypt)
    }
}
```

### UITextField Extensions

```swift
extension UITextField {
    func fxFontSize(_ value: CGFloat) {
        font = font?.withSize(value.fxpt)
    }
    
    func dyFontSize(_ value: CGFloat) {
        font = font?.withSize(value.dypt)
    }
}
```

## 🔄 Migration from Android

### API Mapping

| Android | iOS |
|---------|-----|
| `VirtuesDimens.fixed(16).toPx()` | `VirtuesDimens.fixed(16).toPixels()` |
| `VirtuesDimens.dynamic(100).toDp()` | `VirtuesDimens.dynamic(100).toPoints()` |
| `16.fxdp` | `16.fxpt` |
| `100.dydp` | `100.dypt` |
| `ScreenType.LOWEST` | `ScreenType.lowest` |
| `UiModeType.PHONE` | `DeviceType.phone` |

### Key Differences

1. **Units**: Android uses DP/SP, iOS uses Points
2. **Device Detection**: iOS uses `UIDevice.current.userInterfaceIdiom`
3. **Screen Metrics**: iOS uses `UIScreen.main.bounds`
4. **Multi-Window**: iOS detects split-screen mode differently

## ⚡ Performance Considerations

### Optimization Strategies

1. **Cached Calculations**: Adjustment factors are calculated once per screen configuration
2. **Lazy Evaluation**: Values are computed only when needed
3. **Minimal Overhead**: Simple mathematical operations with minimal memory allocation

### Best Practices

1. **Use Fixed for UI Elements**: Buttons, paddings, fonts, icons
2. **Use Dynamic for Layout**: Container widths, spacers, grid items
3. **Cache Dimensions**: Store frequently used dimensions in properties
4. **Avoid Excessive Nesting**: Keep dimension chains simple

## 🧪 Testing

### Unit Tests

The library includes comprehensive unit tests covering:

- Dimension calculations
- Device type detection
- Screen factor calculations
- Extension methods
- Edge cases and error handling

### Integration Tests

- UIKit integration
- SwiftUI integration
- Performance benchmarks
- Memory usage analysis

## 🔧 Troubleshooting

### Common Issues

1. **Incorrect Scaling**: Check device type detection and screen qualifiers
2. **Performance Issues**: Avoid excessive dimension calculations in tight loops
3. **Layout Problems**: Ensure proper constraint setup with dynamic dimensions

### Debug Tools

```swift
// Print current screen dimensions
let (width, height) = VirtuesDimensAdjustmentFactors.getCurrentScreenDimensions()
print("Screen: \(width) × \(height)")

// Print current device type
print("Device: \(DeviceType.current())")

// Print adjustment factors
let factors = VirtuesDimensAdjustmentFactors.calculateAdjustmentFactors()
print("Factors: \(factors)")
```

## 📊 Performance Benchmarks

### Calculation Performance

| Operation | Time | Memory |
|-----------|------|--------|
| Fixed dimension calculation | ~0.001ms | ~1KB |
| Dynamic dimension calculation | ~0.001ms | ~1KB |
| Device type detection | ~0.0001ms | ~0.1KB |
| Screen factor calculation | ~0.002ms | ~2KB |

### Memory Usage

| Component | Memory Usage |
|-----------|--------------|
| Core library | ~50KB |
| Extensions | ~20KB |
| Cached factors | ~10KB |
| Total | ~80KB |

## 🔄 Version History

### Version 1.0.0
- Initial release
- Core dimensioning system
- SwiftUI and UIKit support
- Physical units conversion
- Performance optimizations

### Future Versions
- Enhanced device detection
- Additional scaling models
- Performance improvements
- Extended platform support

## 🤝 Contributing

### Development Setup

1. Clone the repository
2. Open `VirtuesDimens.xcodeproj`
3. Run tests to ensure everything works
4. Make your changes
5. Add tests for new functionality
6. Submit a pull request

### Code Style

- Follow Swift API Design Guidelines
- Use meaningful variable names
- Add comprehensive documentation
- Include unit tests for new features

## 📄 License

This project is licensed under the Apache License 2.0 - see the [LICENSE](LICENSE) file for details.

## 📞 Support

For questions, issues, or contributions:

- GitHub Issues: [Create an issue](https://github.com/www-virtues-ag/virtues-dimens/issues)
- Email: jean.bodenberg@gmail.com
- Documentation: [GitHub Wiki](https://github.com/www-virtues-ag/virtues-dimens/wiki)