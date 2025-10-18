# 📱 VirtuesDimens iOS - Guia de Instalação

Este guia detalha como instalar e configurar a biblioteca VirtuesDimens iOS em diferentes cenários.

## 🎯 Escolha da Instalação

### Para Apps iOS Padrão (Recomendado)
```ruby
pod 'VirtuesDimens'
```
Inclui automaticamente os módulos Core e UI.

### Para Desenvolvimento de Jogos
```ruby
pod 'VirtuesDimens/Core'
pod 'VirtuesDimens/Games'
```

### Para Bibliotecas que Precisam Apenas de Cálculos
```ruby
pod 'VirtuesDimens/Core'
```

## 📦 Instalação com CocoaPods

### 1. Adicionar ao Podfile

```ruby
# Podfile
platform :ios, '13.0'
use_frameworks!

target 'MyApp' do
  # Para apps iOS padrão
  pod 'VirtuesDimens'
  
  # OU para desenvolvimento de jogos
  pod 'VirtuesDimens/Core'
  pod 'VirtuesDimens/Games'
  
  # OU apenas o módulo Core
  pod 'VirtuesDimens/Core'
end
```

### 2. Instalar Dependências

```bash
pod install
```

### 3. Abrir o Workspace

```bash
open MyApp.xcworkspace
```

## 📦 Instalação com Swift Package Manager

### 1. Adicionar Dependência

No Xcode:
1. File → Add Package Dependencies
2. URL: `https://github.com/www-virtues-ag/virtues-dimens.git`
3. Version: `1.0.0` ou superior
4. Adicionar ao target

### 2. Importar nos Arquivos

```swift
import VirtuesDimens        // Para instalação completa
import VirtuesDimensCore    // Para apenas o módulo Core
import VirtuesDimensUI      // Para módulo UI
import VirtuesDimensGames   // Para módulo Games
```

## 🔧 Configuração Inicial

### Para Apps iOS Padrão

```swift
import VirtuesDimens

@main
struct MyApp: App {
    var body: some Scene {
        WindowGroup {
            DimensProvider {
                ContentView()
            }
        }
    }
}
```

### Para Jogos com Metal

```swift
import VirtuesDimensGames
import Metal

class GameViewController: UIViewController {
    private var metalDevice: MTLDevice!
    private var metalManager: VirtuesDimensMetal!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        // Configurar Metal
        metalDevice = MTLCreateSystemDefaultDevice()
        
        // Configurar viewport
        let viewport = MTLViewport(
            originX: 0, originY: 0,
            width: Double(view.bounds.width),
            height: Double(view.bounds.height),
            znear: 0.0, zfar: 1.0
        )
        
        // Inicializar VirtuesDimens Games
        VirtuesDimensGames.shared.initialize(device: metalDevice, viewport: viewport)
        metalManager = VirtuesDimensGames.shared.getMetalManager()
    }
}
```

## 🎨 Exemplos de Uso

### SwiftUI

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

### UIKit

```swift
import VirtuesDimens

class ViewController: UIViewController {
    override func viewDidLoad() {
        super.viewDidLoad()
        
        let button = UIButton()
        button.fxTitleFontSize(16)
        button.fxCornerRadius(8)
        button.frame = CGRect(x: 0, y: 0, width: 200.dypt, height: 44.fxpt)
        view.addSubview(button)
    }
}
```

### Metal/Games

```swift
import VirtuesDimensGames
import Metal

class GameRenderer {
    func renderUI() {
        let buttonSize = VirtuesDimensGames.uniform(64.0)
        let fontSize = VirtuesDimensGames.aspectRatio(24.0)
        let spacing = VirtuesDimensGames.viewport(16.0)
        
        // Renderizar elementos de UI
    }
}
```

## 🔍 Verificação da Instalação

### Verificar Módulos Disponíveis

```swift
import VirtuesDimens

let availableModules = VirtuesDimensInfo.availableModules()
print("Módulos disponíveis: \(availableModules)")

let libraryInfo = VirtuesDimensInfo.info()
print("Informações da biblioteca: \(libraryInfo)")
```

### Teste Básico

```swift
import VirtuesDimens

// Teste de dimensão fixa
let fixedDimension = VirtuesDimens.fixed(16).toPoints()
print("Dimensão fixa: \(fixedDimension)")

// Teste de dimensão dinâmica
let dynamicDimension = VirtuesDimens.dynamic(100).toPoints()
print("Dimensão dinâmica: \(dynamicDimension)")

// Teste de sintaxe simplificada
let simpleFixed = 16.fxpt
let simpleDynamic = 100.dypt
print("Sintaxe simplificada - Fixo: \(simpleFixed), Dinâmico: \(simpleDynamic)")
```

## 🚨 Solução de Problemas

### Erro: "No such module 'VirtuesDimens'"

1. Verifique se o pod foi instalado corretamente:
   ```bash
   pod install
   ```

2. Limpe o build:
   ```bash
   Product → Clean Build Folder
   ```

3. Rebuild o projeto

### Erro: "VirtuesDimensGames not found"

1. Verifique se o módulo Games foi incluído:
   ```ruby
   pod 'VirtuesDimens/Games'
   ```

2. Verifique se Metal está disponível no dispositivo

### Erro: "SwiftUI not available"

1. Verifique se o iOS deployment target é 13.0 ou superior
2. Use apenas o módulo Core se SwiftUI não estiver disponível

## 📱 Requisitos do Sistema

### iOS
- iOS 13.0 ou superior
- Xcode 12.0 ou superior
- Swift 5.0 ou superior

### Módulo Games
- Metal disponível (iOS 8.0+)
- MetalKit disponível (iOS 8.0+)

### Módulo UI
- SwiftUI disponível (iOS 13.0+)

## 🔄 Atualização

### CocoaPods

```bash
pod update VirtuesDimens
```

### Swift Package Manager

No Xcode:
1. File → Package Dependencies
2. Selecionar VirtuesDimens
3. Update to Latest Package Versions

## 📚 Próximos Passos

Após a instalação, consulte:

- [Guia de Uso](USAGE_GUIDE.md)
- [Documentação da API](API_REFERENCE.md)
- [Exemplos](Examples/)
- [FAQ](FAQ.md)

## 🤝 Suporte

Para dúvidas ou problemas:

- [GitHub Issues](https://github.com/www-virtues-ag/virtues-dimens/issues)
- Email: jean.bodenberg@gmail.com
- [Documentação](https://github.com/www-virtues-ag/virtues-dimens/wiki)
