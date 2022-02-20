Приложения для изучения английских слов

REST API

> GET http://localhost:8888/vocabulary
>
> Query example: word = resolve
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

> POST http://localhost:8888/vocabulary
>
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

> GET http://localhost:8888/audio/uk/mp3
> Query example: fileName=resolve
> Response example: file resolve.mp3 (английское произношение)

> GET http://localhost:8888/audio/us/mp3
> Query example: fileName=resolve
> Response example: file resolve.mp3 (американское произношение)