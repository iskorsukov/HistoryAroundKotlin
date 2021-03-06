# HistoryAround / История вокруг
Приложение, которое позволяет просматривать информацию с Википедии о местах вокруг пользователя, опираясь на его местоположение.

# Особенности
- Найдите интересные места или узнайте историю мест, которые видите каждый день
- Сохраняйте понравившиеся статьи в избранное для быстрого доступа к ним
- Настраивайте радиус поиска и язык статей, ищите историю вокруг дома и за границей

# Особенности имплементации
- Архитектура MVI (ViewModel + LiveData + custom ViewState с LCE состояниями и UI действиями)
- Android Navigation Component используется для навигации и отображения фрагментов
- Room используется для хранения избранного
- Retrofit 2 + Gson используется для логики запросов и взамодействия с БД
- OSMDroid используется для виджета карты
- Google API (FusedLocationProviderClient) используется для определения местоположения
- Dagger 2 используется для иньекции зависимостей
- Glide используется для работы с изображениями
- JUnit, Mockito и Espresso используется для тестов на устройстве
