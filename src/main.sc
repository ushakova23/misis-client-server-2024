require: slotfilling/slotFilling.sc
  module = sys.zb-common
  
require: js/script.js
theme: /

    state: Start
        q!: $regex</start>
        a: Добро пожаловать! Тут можно узнать рецепты полезных напитков. Для начала нужно выбрать рецепт, например «Открой какао с бананом»
        buttons:
            "Открой какао с бананом"
            "Помощь"
            "Выход"

    state: Приветствие
        intent!: /привет
        a: Добро пожаловать! Тут можно узнать рецепты полезных напитков. Для начала нужно выбрать рецепт, например «Открой какао с бананом»
        buttons:
            "Открой какао с бананом"
            "Помощь"
            "Выход"
            
    state: ВыходВГлавноеМеню
        q!: (к списку рецептов|список рецептов)
        script:
            addAction({
                type: "goto_home"
            }, $context);
        buttons:
            "Открой чай матча"
            "Помощь"
            "Выход"
        
    state: ПереходРецептПоНазванию
        q!: [давай] [приготовим|сделаем] [открой] *
        if: findItemIdBySelectedItem(getRequest($context)) == null
            a: Я не понимаю. Можно выбрать напиток, например, «Открой какао с бананом», или использовать команду "К списку рецептов".
        
        script:
            var itemId = findItemIdBySelectedItem(getRequest($context));
            
            log($context.request.rawRequest.payload.meta.current_app.state)
            
            if (itemId !== null) {
                addAction({
                    type: "goto_drink",
                    id: itemId
                }, $context);
            }
        buttons:
            "Открой чай матча"
            "К списку рецептов"
            "Помощь"
            
    
    state: ПереходНаРецепт
        event!: PAGE_OPEN
        script:
            log("PAGE_OPEN_EVENT")
        if: $request.data.eventData.recipe
            a: Для начала приготовления необходимо нажать кнопку "Начать готовить". || auto_listening = false
        else: 
            a: Что-то пошло не так || auto_listening = false
        buttons:
            "К списку рецептов"
            "Помощь"
            
            
    
    state: КнопкаНачатьГотовить
        event!: START_COOK
        if: $request.data.eventData.recipe
            a: Отлично! Вот список ингредиентов, которые понадобятся {{$request.data.eventData.ingredients}}. Итак, {{$request.data.eventData.recipe}} || auto_listening = false
        else: 
            a: Что-то пошло не так || auto_listening = false
        buttons:
            "К списку рецептов"
            "Помощь"

    state: ПереходРецептПоНомеру
        q!: [давай] (приготовим|сделаем) @duckling.number:: digit
        a: Хорошо, {{$parseTree.text}}! || auto_listening = false
        script:
            var itemId = findItemIdByNumber($parseTree._digit, getRequest($context));
            
            if (itemId !== null) {
                addAction({
                    type: "goto_drink",
                    id: itemId
                }, $context);
            }
        buttons:
            "К списку рецептов"
            "Помощь"
    
    state: ВыходВМенюКнопкойНаСайтеИлиНаПульте
        event!: OPEN_MENU_BTN
        script:
            log("OPEN_MENU_BTN")
        a: Список рецептов уже на экране || auto_listening = false
        buttons:
            "Открой какао с бананом"
            "Выход"
        
    state: Прощание
        intent!: /пока
        a: Пока пока
    
    state: Помощь
        q!: помощь
        a: Для начала нужно выбрать напиток, например «Открой какао с бананом». Затем необходимо нажать кнопку "Начать готовить". 
        buttons:
            "Открой какао с бананом"
            "Выход"
        

    state: Fallback || noContext=true
        event!: noMatch
        a: Я не понимаю. Можно выбрать напиток, например, «Открой какао с бананом», или использовать команду "К списку рецептов".

