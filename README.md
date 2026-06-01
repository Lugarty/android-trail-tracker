# Trail Tracker Android

Aplicativo Android desenvolvido em Java utilizando a API do Google Maps para registrar, visualizar e gerenciar trilhas geográficas em tempo real.

## 📱 Sobre o Projeto

O aplicativo permite que o usuário registre percursos utilizando a localização do dispositivo, acompanhe informações da trilha e visualize trajetos diretamente no mapa.

O projeto foi desenvolvido com foco na integração entre Android, GPS e Google Maps.

## 🚀 Funcionalidades

* Registro de trilhas utilizando GPS
* Exibição da localização em tempo real
* Visualização de trajetos no mapa
* Cálculo da distância percorrida
* Exibição da velocidade atual e máxima
* Consulta de trilhas salvas
* Edição do nome das trilhas
* Exclusão de trilhas individuais ou de todos os registros
* Configurações de visualização do mapa

## 🛠 Tecnologias Utilizadas

* Java
* Android Studio
* Google Maps SDK for Android
* Fused Location Provider
* SQLite
* RecyclerView
* Material Design

## 📂 Estrutura do Projeto

```text
app/
├── activities/
│   ├── RegistrarTrilhaActivity
│   ├── VisualizarTrilhaActivity
│   ├── ConsultarTrilhasActivity
│   └── ConfigActivity
├── adapter/
├── database/
└── res/
```

## ⚙️ Requisitos

* Android Studio
* Android SDK 23 ou superior
* Google Play Services
* Chave da API do Google Maps

## 🔑 Configuração da API do Google Maps

Por questões de segurança, a chave da API não está incluída neste repositório.

1. Acesse o Google Cloud Console.
2. Crie um projeto.
3. Ative o Maps SDK for Android.
4. Gere uma chave de API.
5. Substitua o valor no arquivo `AndroidManifest.xml`:

```xml
<meta-data
    android:name="com.google.android.geo.API_KEY"
    android:value="SUA_CHAVE_AQUI" />
```

## ▶️ Como Executar

1. Clone o repositório:

```bash
git clone https://github.com/Lugarty/android-trail-tracker
```

2. Abra o projeto no Android Studio.

3. Aguarde a sincronização do Gradle.

4. Configure sua chave da API do Google Maps.

5. Execute o aplicativo em um dispositivo físico ou emulador com Google Play Services.

## 👥 Desenvolvedor

### 🌟 Nome	| 📧 Contato
- Anisio Oliveira Albuquerque Filho	| anisioalbuquerque71@gmail.com
