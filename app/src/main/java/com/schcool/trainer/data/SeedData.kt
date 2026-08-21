package com.schcool.trainer.data

import com.schcool.trainer.domain.Difficulty

object SeedData {
    val phones = listOf(
        PhoneEntity(
            brand = "Samsung",
            model = "Galaxy A55",
            priceRub = 39990,
            os = "Android",
            memory = "8/256 GB",
            camera = "50+12+5 MP",
            battery = "5000 mAh",
            screen = "6.6\" AMOLED 120Hz",
            connectivity = "5G, NFC, Wi‑Fi 6, BT 5.3",
            advantages = "Яркий экран, защита IP67, стабильная камера",
            limitations = "Нет беспроводной зарядки"
        ),
        PhoneEntity(
            brand = "Xiaomi",
            model = "Redmi Note 13 Pro",
            priceRub = 32990,
            os = "Android",
            memory = "8/256 GB",
            camera = "200+8+2 MP",
            battery = "5100 mAh",
            screen = "6.67\" AMOLED 120Hz",
            connectivity = "5G, NFC, Wi‑Fi 5, BT 5.2",
            advantages = "Сильная камера и быстрая зарядка",
            limitations = "Много предустановленных сервисов"
        ),
        PhoneEntity(
            brand = "Apple",
            model = "iPhone 15",
            priceRub = 79990,
            os = "iOS",
            memory = "128 GB",
            camera = "48+12 MP",
            battery = "До 20 ч видео",
            screen = "6.1\" OLED",
            connectivity = "5G, NFC, Wi‑Fi 6, MagSafe",
            advantages = "Экосистема, производительность, поддержка обновлений",
            limitations = "Высокая стоимость и дорогие аксессуары"
        )
    )

    val scenarios = listOf(
        ScenarioEntity(
            name = "Первый покупатель",
            customerType = "Осторожный и экономный",
            difficulty = Difficulty.EASY,
            objections = "Дорого, не уверен в бренде",
            expectedApproach = "Уточнить бюджет, приоритеты, показать выгоду за цену",
            rubricHints = "Нужны вопросы о потребностях и корректное сравнение"
        ),
        ScenarioEntity(
            name = "Сравнитель",
            customerType = "Постоянно сравнивает с интернетом",
            difficulty = Difficulty.MEDIUM,
            objections = "У конкурента дешевле, в обзорах жалуются на камеру",
            expectedApproach = "Подтвердить информацию, отработать возражения фактами",
            rubricHints = "Нужны аргументы, преимущества и честные ограничения"
        ),
        ScenarioEntity(
            name = "Противный эксперт",
            customerType = "Сложный, перебивает, задает много каверзных вопросов",
            difficulty = Difficulty.HARD,
            objections = "Сомнения по гарантии, автономности, играм и рассрочке",
            expectedApproach = "Спокойствие, структура, закрытие на конкретный выбор",
            rubricHints = "Нужны четкие ответы, контроль диалога и попытка закрытия"
        )
    )

    val annoyingQuestionTemplates = listOf(
        "Почему у вас так дорого?",
        "Что если батарея просядет через год?",
        "А в игры типа Genshin потянет без лагов?",
        "Почему не взять дешевле на маркетплейсе?",
        "Сколько обновлений реально получу?",
        "Докажите, что камера ночью не мылит.",
        "Если сломается, кто и как чинит по гарантии?",
        "Чем эта модель лучше прошлогодней?",
        "Сравните с конкурентом за те же деньги.",
        "Рассрочка без переплаты точно без скрытых условий?",
        "Сколько держит батарея при навигаторе и мессенджерах?",
        "Почему здесь память UFS важна обычному человеку?",
        "Что с перегревом летом?",
        "Есть eSIM и как ее перенести?",
        "Какая защита от воды — это реально работает?",
        "Какие компромиссы у этой модели, говорите честно."
    )
}
