# Recipe App

Android-приложение для поиска и просмотра рецептов с каталогом категорий, избранными рецептами и пошаговыми инструкциями приготовления.

<p align="center">
  <img src="https://github.com/user-attachments/assets/de29f874-392c-418f-ac6d-bcc2f3dca25a" width="20%"/> &nbsp;&nbsp;&nbsp;
  <img src="https://github.com/user-attachments/assets/1122ff92-925d-4a5c-8c7b-2fe6f70d2d6a" width="20%"/> &nbsp;&nbsp;&nbsp;
  <img src="https://github.com/user-attachments/assets/e7eb74fd-18e1-49b4-a53c-3454a6836fc5" width="20%"/>
</p>

## О проекте

Приложение позволяет просматривать рецепты различных категорий, получать подробную информацию о блюдах, сохранять понравившиеся рецепты в избранное и изменять количество порций с автоматическим пересчётом ингредиентов.

## Основные возможности

- Просмотр категорий блюд
- Каталог рецептов по категориям
- Детальная информация о рецепте
- Пошаговые инструкции приготовления
- Список ингредиентов
- Избранные рецепты
- Автоматический пересчёт ингредиентов под выбранное количество порций
- Работа без интернета для сохранённых данных

## Технологии

- UI: XML + Material Design + ViewBinding
- Architecture: MVVM + Repository Pattern + LiveData
- Network: Retrofit + OkHttp
- Database: Room
- Async: Kotlin Coroutines
- DI: Hilt(Dagger)

## Установка

1. Клонировать репозиторий

```bash
git clone <repository-url>
```

2. Открыть проект в Android Studio

3. Выполнить синхронизацию Gradle

4. Запустить приложение на устройстве или эмуляторе
