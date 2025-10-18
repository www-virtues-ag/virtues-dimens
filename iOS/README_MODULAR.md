# 📱 VirtuesDimens iOS - Estrutura Modular

[![Version](https://img.shields.io/cocoapods/v/VirtuesDimens.svg?style=flat)](https://cocoapods.org/pods/VirtuesDimens)
[![License](https://img.shields.io/cocoapods/l/VirtuesDimens.svg?style=flat)](https://cocoapods.org/pods/VirtuesDimens)
[![Platform](https://img.shields.io/cocoapods/p/VirtuesDimens.svg?style=flat)](https://cocoapods.org/pods/VirtuesDimens)

**VirtuesDimens** é um sistema de gerenciamento de dimensões responsivas para iOS que ajusta automaticamente valores baseados nas dimensões da tela, garantindo consistência de layout em qualquer tamanho ou proporção de tela.

## 🏗️ Arquitetura Modular

A biblioteca VirtuesDimens foi reorganizada em uma estrutura modular para permitir escolha flexível dos componentes necessários:

### 📦 Módulos Disponíveis

| Módulo | Descrição | Dependências |
|--------|-----------|--------------|
| **Core** | Funcionalidade básica de gerenciamento de dimensões | Foundation, UIKit |
| **UI** | Extensões para UIKit e SwiftUI | Core + SwiftUI |
| **Games** | Funcionalidade específica para Metal/games | Core + Metal + MetalKit |

## 🚀 Instalação

### CocoaPods

#### Instalação Completa (Recomendado)
```ruby
pod 'VirtuesDimens'
```
Isso inclui automaticamente os módulos Core e UI.

#### Instalação Modular
```ruby
# Apenas o módulo Core
pod 'VirtuesDimens/Core'

# Core + UI (padrão)
pod 'VirtuesDimens/UI'

# Core + Games (para desenvolvimento de jogos)
pod 'VirtuesDimens/Games'

# Todos os módulos
pod 'VirtuesDimens/Core'
pod 'VirtuesDimens/UI'
pod 'VirtuesDimens/Games'
```

### Swift Package Manager

```swift
.package(url: "https://github.com/www-virtues-ag/virtues-dimens.git", from: "1.0.0")
```

## 📖 Uso por Módulo

### 🔧 Módulo Core

O módulo Core fornece a funcionalidade básica de gerenciamento de dimensões:

```swift
import VirtuesDimensCore

// Uso básico
let buttonHeight = VirtuesDimens.fixed(48).toPoints()
let cardWidth = VirtuesDimens.dynamic(100).toPoints()

// Sintaxe simplificada
let padding = 16.fxpt
let margin = 100.dypt

// Unidades físicas
let width = 2.cm
let height = 1.inch
```

### 🎨 Módulo UI

O módulo UI adiciona integração com UIKit e SwiftUI:

```swift
import VirtuesDimensUI

// SwiftUI
struct ContentView: View {
    var body: some View {
        VStack(spacing: 20.fxpt) {
            Text("Título")
                .font(.fxSystem(size: 24, weight: .bold))
                .fxPadding(16)
            
            Rectangle()
                .fxFrame(width: 200, height: 100)
                .fxCornerRadius(12)
        }
    }
}

// UIKit
let button = UIButton()
button.fxTitleFontSize(16)
button.fxCornerRadius(8)
```

### 🎮 Módulo Games

O módulo Games fornece funcionalidade específica para desenvolvimento de jogos com Metal:

```swift
import VirtuesDimensGames
import Metal

// Inicialização
let device = MTLCreateSystemDefaultDevice()!
let viewport = MTLViewport(originX: 0, originY: 0, width: 1920, height: 1080, znear: 0, zfar: 1)
VirtuesDimensGames.shared.initialize(device: device, viewport: viewport)

// Uso em jogos
let buttonSize = VirtuesDimensGames.uniform(64.0)
let fontSize = VirtuesDimensGames.aspectRatio(24.0)
let spacing = VirtuesDimensGames.viewport(16.0)

// Extensões para simd
let position = simd_float2(100, 200)
let scaledPosition = position.gameUniform(VirtuesDimensGames.shared.getMetalManager()!)

// Conversão de coordenadas
let ndcPoint = VirtuesDimensGames.shared.screenToNDC(simd_float2(960, 540))
```

## 🎯 Escolha do Módulo

### Para Apps iOS Padrão
```ruby
pod 'VirtuesDimens'  # Inclui Core + UI automaticamente
```

### Para Desenvolvimento de Jogos
```ruby
pod 'VirtuesDimens/Core'
pod 'VirtuesDimens/Games'
```

### Para Bibliotecas que Precisam Apenas de Cálculos
```ruby
pod 'VirtuesDimens/Core'
```

### Para Apps que Usam Apenas UIKit (sem SwiftUI)
```ruby
pod 'VirtuesDimens/Core'
# Use apenas as extensões UIKit do módulo Core
```

## 🔧 Configuração Avançada

### Módulo Core
```swift
// Configuração customizada
let customDimension = VirtuesDimens.fixed(16)
    .screen(.phone, 14)           // 14pt para iPhones
    .screen(.tablet, 18)          // 18pt para iPads
    .aspectRatio(enable: true)    // Ativar ajuste de proporção
    .toPoints()
```

### Módulo Games
```swift
// Configuração de performance
let performanceSettings = GamePerformanceSettings.highPerformance

// Diferentes modos de escalonamento
let uniformSize = VirtuesDimensGames.uniform(100.0)      // Escalonamento uniforme
let horizontalSize = VirtuesDimensGames.horizontal(100.0) // Escalonamento horizontal
let viewportSize = VirtuesDimensGames.viewport(100.0)     // Escalonamento por viewport
```

## 📊 Comparação de Módulos

| Funcionalidade | Core | UI | Games |
|----------------|------|----|----|
| Cálculos básicos | ✅ | ✅ | ✅ |
| Extensões UIKit | ✅ | ✅ | ✅ |
| Extensões SwiftUI | ❌ | ✅ | ❌ |
| Integração Metal | ❌ | ❌ | ✅ |
| Coordenadas NDC | ❌ | ❌ | ✅ |
| Escalonamento de jogos | ❌ | ❌ | ✅ |
| Tamanho da biblioteca | ~50KB | ~80KB | ~120KB |

## 🎨 Exemplos de Uso

### App iOS Padrão
```swift
import VirtuesDimens

struct ContentView: View {
    var body: some View {
        VStack(spacing: 20.fxpt) {
            Text("VirtuesDimens")
                .font(.fxSystem(size: 24, weight: .bold))
                .fxPadding(16)
            
            Rectangle()
                .fxFrame(width: 200, height: 100)
                .fxCornerRadius(12)
        }
    }
}
```

### Jogo com Metal
```swift
import VirtuesDimensGames
import Metal

class GameRenderer {
    private let metalManager: VirtuesDimensMetal
    
    init(device: MTLDevice, viewport: MTLViewport) {
        self.metalManager = VirtuesDimensMetal(device: device, viewport: viewport)
        VirtuesDimensGames.shared.initialize(device: device, viewport: viewport)
    }
    
    func renderUI() {
        let buttonSize = 64.0.gameUniform(metalManager)
        let fontSize = 24.0.gameFontSize(metalManager)
        let spacing = 16.0.gameSpacing(metalManager)
        
        // Renderizar elementos de UI com dimensões escalonadas
    }
}
```

## 📚 Documentação Adicional

- [Documentação Core](Sources/VirtuesDimensCore/README.md)
- [Documentação UI](Sources/VirtuesDimensUI/README.md)
- [Documentação Games](Sources/VirtuesDimensGames/README.md)
- [Guia de Migração](MIGRATION_GUIDE.md)

## 🤝 Contribuição

Contribuições são bem-vindas! Por favor, sinta-se à vontade para enviar um Pull Request.

## 📄 Licença

Este projeto está licenciado sob a Licença Apache 2.0 - veja o arquivo [LICENSE](LICENSE) para detalhes.

## 👨‍💻 Autor

**Jean Bodenberg**
- GitHub: [@bodenberg](https://github.com/www-virtues-ag)
- Email: jean.bodenberg@gmail.com
