require: slotfilling/slotFilling.sc
  module = sys.zb-common
  
require: js/script.js
theme: /

    state: Start
        q!: $regex</start>
        a: Добро пожаловать! Тут можно узнать рецепты полезных коктейлей. Для начала нужно выбрать напиток, например «Давай приготовим какао с бананом»
        buttons:
            "Давай приготовим какао с бананом"

    state: Приветствие
        intent!: /привет
        a: Добро пожаловать! Тут можно узнать рецепты полезных коктейлей. Для начала нужно выбрать напиток, например «Давай приготовим какао с бананом»
        buttons:
            "Давай приготовим какао с бананом"
            
    state: ВыходВГлавноеМеню
        q!: (к списку рецептов|список рецептов)
        a: Хорошо
        script:
            addAction({
                type: "goto_home"
            }, $context);
        buttons:
            "Давай приготовим чай матча"
        
    state: ПереходРецептПоНазванию
        q!: [давай] (приготовим|сделаем) *
        if: findItemIdBySelectedItem(getRequest($context)) == null
            a: Такого рецепта нет
        else:
            a: ""
        script:
            var itemId = findItemIdBySelectedItem(getRequest($context));
            
            log($context.request.rawRequest.payload.meta.current_app.state)
            
            if (itemId !== null) {
                addAction({
                    type: "goto_drink",
                    id: itemId
                }, $context);
            }
    
    state: ПереходНаРецепт
        event!: PAGE_OPEN
        if: $request.data.eventData.recipe
            a: Для начала приготовления необходимо нажать кнопку "Начать готовить".
        else: 
            a: Что-то пошло не так
            
    
    state: КнопкаНачатьГотовить
        event!: START_COOK
        if: $request.data.eventData.recipe
            a: Отлично! Вот список инградиентов, которые понадобятся {{$request.data.eventData.ingredients}}. Итак, {{$request.data.eventData.recipe}}
        else: 
            a: Что-то пошло не так
        buttons:
            "К списку рецептов"
            "Давай приготовим чай матча"
    
    state: ПереходРецептПоНомеру
        q!: [давай] (приготовим|сделаем) @duckling.number:: digit
        a: Хорошо, {{$parseTree.text}}!
        script:
            var itemId = findItemIdByNumber($parseTree._digit, getRequest($context));
            
            if (itemId !== null) {
                addAction({
                    type: "goto_drink",
                    id: itemId
                }, $context);
            }
    

    state: Прощание
        intent!: /пока
        a: Пока пока

    state: Fallback || noContext=true
        event!: noMatch
        a: Я не понимаю. Можно выбрать напиток, например, «Давай приготовим какао с бананом», или использовать команду "К списку рецептов".


