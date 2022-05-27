
# Rapport

Projektuppgiften handlade om att smida ihop alla de delar vi jobbat med under kursens gång, till större och mer komplett. Appen som utvecklades hämtade och presenterade information om äpplen. Denna information hämtades från en api erhållen av. JSON-datan som appen jobbar med ser ut enligt kodblocket nedan:

```json
{
	"ID": "discovery",
	"name": "Discovery",
	"type": "a21oligu",
	"company": "",
	"location": "England",
	"category": "",
	"size": 0,
	"cost": 0,
	"auxdata": {
		"img": "länk_till_bild.png",
		"color": [
			"red",
			"green"
		],
		"characteristics": ["fresh", "hard shell"]
	}
}
```

Fältet id är namnet på äpplet i lowercased snakecase. Name är äpplets namn och location äpplets ursprung. I auxdata finns extra fält. Fältet img är en länk till en bildresurs som hämtades och renderades i appen. Auxdata består även av två arrayer med strängar: color och characteristics. Dessa används senare för att filtrera listan av äpplen.

Huvudskärmen av appen består av en RecyclerView. Denna har en Adapter som initerades från en egen klass, AppleAdapter. Denna klass sköter uppdaterande och liknande av RecyclerViewen, men har även egen implementerade fnuktioner för att filtrera listan med äpplen. Varje äpple i denna lista är av klassen Apple. Det är denna klass som json-datan bryts ner till i MainActivity#onPostExecute, se kodblock nedan:

```Java
public void onPostExecute(String json) {
    Type type = new TypeToken<ArrayList<Apple>>() {}.getType();
    
    ...

    try {
        ...
        
        ArrayList<Apple> newApples = gson.fromJson(json, type);
        appleAdapter.addApples(newApples);
        
        ...
        
    } catch (Exception e) {
        e.printStackTrace();
    }
}
```

En viktig del av appen var filtreringsfunktionen. En filtrerings knapp lades till i menu knappen höger om toolbaren. Menyn lades till i menymappen i mappen *res*, och implementerades sedan i MainActivity. Samma meny användes för att lägga till en knapp för about-skärmen. Genom att trycka denna skickas användaren vidare till about-skärmen där den kan läsa om appen. När filtreringsknappen trycks öppnas en lista med olika val av filtreringstyper. Genom att klicka på en av dessa kommer listan filtreras och RecyclerViewen uppdateras. Satta filter sparas även i SharedReferences med följande funktion i MainActivity:

```Java
private void saveFilter(String type, String filter) {
    SharedPreferences.Editor editor = sharedPreferences.edit(); // från global instans
    editor.putString("type", type).putString("filter", filter).apply();
}
```

En annan funktion som appen har är att vid ett klick på en list_item, skicka användaren till en detaljvy. I denna detaljvy finns information om äpplet som inte presenterades på huvudskärmen.

Designen av appen var till stor del planerad redan innan appen programmerades. En prototyp skapades med verktyget figma och låg i grunden för hur vyer och listor var placerade. Detta underlättade arbetet och gav mer tid att lägga fokus på funktionallitet i appen. Skissen som togs fram går att se i bilden nedan:

<img src="https://cdn.discordapp.com/attachments/931726768178626573/979750687523098634/unknown.png" height="500px" />

Värt att notera om just skissen är att små ändringar gjorde under arbetets gång, något jag lät vara oändrat i skissen.

## Implementationsexempel VG

### Det har tidigare i rapporten tagits upp hur jag löste VG kraven. Kommande är de jag inte tagit upp ännu eller mer ingående.

Varje list_item i RecyclreViewen innehåller två Views som manipuleras av json-datan. 

Det går att filtrera äpplen efter färg och karaktärer. Det valde filtret sparas vid val så att det finns kvar när användaren startar upp appen igen. Denna information om filter sparas i SharedPreferences.

Detaljvyn som går att komma till via genom att trycka på ett list_item presenterar mer detaljerad information om äpplet man tryckt på. Här kan även en större bild av äpplet ses. Tillbaka knappen vänster av toolbaren gör det enkelt att återgå till huvudskärmen.

Datan som presenteras i detaljvyn skickas från huvudskärmen genom en intent. Objektet bryts ner till json som sedan "parsas" tillbaka vid detaljvyn.

### Bilder från appen

<img src="app_main" height="400px" />
<img src="app_filter" height="400px" />
<img src="app_detail" height="400px" />
<img src="app_about" height="400px" />
