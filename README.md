## Приложения для изучения английских слов

### Telegram bot [English Learn](https://t.me/EngVocabularyTrainBot)

> Бот знает следующие команды:
> - **/start** - запускает бота
> - _"any english word"_ - напишите любое английское слово и получите перевод с транскрипцией и голосовое сообщение в ответ
> - **/examination** = запускает экзамен
> - **/word_info** = команда пока не доступна
> - **/stop** = деактивирует бота

### REST API
<details>
<summary>
GET <a href="http://localhost:8888/vocabulary">/vocabulary</a>
</summary>

> Query example: word=resolve
>
> Response example:
> ```json
> {
>     "word": "resolve",
>     "transcriptionUs": "|rɪˈzɑːlv|",
>     "transcriptionUk": "|rɪˈzɒlv|",
>     "translates": "решимость, решение, решительность, решать, разрешать, решаться"
> }
> ```
</details>

<details>
<summary>
POST <a href="http://localhost:8888/vocabulary">/vocabulary</a>
</summary>

> Body JSON example:
> ```json
> [
>     "abbreviation",
>     "about",
>     "above"
> ]
> ```
>
>
> Response example:
> ```json
> [
>     {
>         "word": "abbreviation",
>         "transcriptionUs": "|əˌbrɪvɪˈeɪʃən|",
>         "transcriptionUk": "|əˌbriːvɪˈeɪʃən|",
>         "translates": "аббревиатура, сокращение, аббревиация"
>     },
>     {
>         "word": "about",
>         "transcriptionUs": "|əˈbaʊt|",
>         "transcriptionUk": "|əˈbaʊt|",
>         "translates": "о, об, около, по, около, меняющий курс, менять курс"
>     },
>     {
>         "word": "above",
>         "transcriptionUs": "|əˈbʌv|",
>         "transcriptionUk": "|əˈbʌv|",
>         "translates": "выше, более, свыше, выше, над, свыше, вышеупомянутое, упомянутый выше"
>     }
> ]
> ```
</details>

<details>
<summary>
GET <a href="http://localhost:8888/audio/uk/mp3">/audio/uk/mp3</a>
</summary>

> Query example: fileName=resolve
>
> Response example: file resolve.mp3 (английское произношение)
</details>

<details>
<summary>
GET <a href="http://localhost:8888/audio/us/mp3">/audio/us/mp3</a>
</summary>

> Query example: fileName=resolve
>
> Response example: file resolve.mp3 (американское произношение)
</details>

<details>
<summary>
GET <a href="http://localhost:8888/learning/words/10">/learning/words/10</a>
</summary>

> Response example:
> ```json
> [
>    "his",
>    "buying",
>    "wednesday",
>    "driver",
>    "goodnight",
>    "small",
>    "useful",
>    "linked",
>    "at",
>    "add"
> ]
> ```
</details>

<details>
<summary>
POST http://localhost:8888/learning/examination
</summary>

> Body JSON example:
> ```json
> {
>     "she": "она",
>     "classroom": "класс",
>     "level": "уровень",
>     "guess": "гость",
>     "buying": "купля",
>     "scenery": "сцена",
>     "just": "только",
>     "chips": "чипсы",
>     "press": "нажимать",
>     "midnight": "полночь",
>     "go": "идти",
>     "ball": "мяч",
>     "october": "октябрь",
>     "board": "доска",
>     "waiter": "официант",
>     "cheap": "не помню",
>     "from": "из",
>     "next": "следующий",
>     "research": "не помню",
>     "pair": "пара"
> }
> ```
>
> Response example:
> ```json
> {
>     "result": "Вы перевели правильно 16 слов(а) из 20",
>     "right": [
>         {
>             "word": "ball",
>             "answer": "мяч",
>             "translate": "мяч, шар, бал, шарик, удар, шаровой, свивать, свиваться"
>         },
>         {
>             "word": "board",
>             "answer": "доска",
>             "translate": "совет, борт, доска, правление, питание, садиться, столоваться, лавировать"
>         },
>         {
>             "word": "buying",
>             "answer": "купля",
>             "translate": "купля"
>         },
>         {
>             "word": "chips",
>             "answer": "чипсы",
>             "translate": "чипсы, кусочки, деньги, ломтики, бабки"
>         },
>         {
>             "word": "classroom",
>             "answer": "класс",
>             "translate": "класс, классная комната, аудитория"
>         },
>         {
>             "word": "from",
>             "answer": "из",
>             "translate": "от, из, с, судя по"
>         },
>         {
>             "word": "go",
>             "answer": "идти",
>             "translate": "идти, ехать, ходить, переходить, ездить, ход, движение, попытка, ходьба"
>         },
>         {
>             "word": "just",
>             "answer": "только",
>             "translate": "просто, только что, как раз, именно, точно, точный, справедливый, верный, заслуженный"
>         },
>         {
>             "word": "level",
>             "answer": "уровень",
>             "translate": "уровень, ступень, ровный, ровно, вровень, выравнивать"
>         },
>         {
>             "word": "midnight",
>             "answer": "полночь",
>             "translate": "полночь, непроглядная тьма, полуночный"
>         },
>         {
>             "word": "next",
>             "answer": "следующий",
>             "translate": "следующий, ближайший, рядом, дальше, затем, около, рядом, около"
>         },
>         {
>             "word": "october",
>             "answer": "октябрь",
>             "translate": "октябрь, октябрьский"
>         },
>         {
>             "word": "pair",
>             "answer": "пара",
>             "translate": "пара, чета, партнеры, парный, спаривать, спариваться"
>         },
>         {
>             "word": "press",
>             "answer": "нажимать",
>             "translate": "пресс, пресса, печать, жим, надавливание, нажимать, давить, настаивать, жать"
>         },
>         {
>             "word": "she",
>             "answer": "она",
>             "translate": "она, та, которая, женщина, самка"
>         },
>         {
>             "word": "waiter",
>             "answer": "официант",
>             "translate": "официант, поднос, подавальщик, кухонный лифт, вращающийся столик для закусок"
>         }
>     ],
>     "wrong": [
>         {
>             "word": "cheap",
>             "answer": "не помню",
>             "translate": "дешевый, низкий, плохой, дешево, дешевка"
>         },
>         {
>             "word": "guess",
>             "answer": "гость",
>             "translate": "предположение, догадка, предполагать, полагать, догадываться"
>         },
>         {
>             "word": "research",
>             "answer": "не помню",
>             "translate": "исследование, исследовательский, исследовать"
>         },
>         {
>             "word": "scenery",
>             "answer": "сцена",
>             "translate": "пейзаж, декорации"
>         }
>     ]
> }
> ```
</details>

### Используемые технологии

> 

### [План разработки](FUTURE.md)