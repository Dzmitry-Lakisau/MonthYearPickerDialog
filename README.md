Have you ever been told to implement picker of month and year only? Then you know that standard [DatePickerDialog](https://developer.android.com/guide/topics/ui/controls/pickers) requires selecting of exact day and makes selecting of month and year long. This library solves both this issues.

Since [this](https://github.com/premkumarroyal/MonthAndYearPicker) and [this](https://github.com/demogorgorn/MonthAndYearPicker) libraries didn't have required features for my back then enterprise projects I have written this new library fully in Kotlin with usage of ideas of above-mentioned libraries. In process I have added a few more features.

## Examples of usage

Creating usual picker is simple: 
<pre>
MonthYearPickerDialog.Builder(
	context = this,
	themeResId = R.style.Style_MonthYearPickerDialog,
	onDateSetListener = { year, month ->
		// do something
	}
)
	.build()
</pre>
You will get a picker with all months and years ranging from 1970 to the current year available for selection.

You can set the initially selected month and year:
<pre>
MonthYearPickerDialog.Builder(
    context = this,
	themeResId = R.style.Style_MonthYearPickerDialog,
	onDateSetListener = { year, month 
		// do something
	},
	<b>selectedYear = initialYear,
	selectedMonth = initialMonth</b>
)
	.build()
</pre>

You can change button's text and add dialog's title with standard method:
<pre>
val dialog = MonthYearPickerDialog.Builder(
	this,
	R.style.Style_MonthYearPickerDialog_Orange,
	{ year, month ->
		// do something
	},
	initialYear,
	initialMonth
)
	<b>.setNegativeButton("Return")
	.setPositiveButton(R.string.set)</b>
	.build()

<b>dialog.setTitle("Select month and year")</b>
</pre>
<img src="https://github.com/Dzmitry-Lakisau/MonthYearPickerDialog/blob/develop/screenshots/Screenshot_01.jpg" width="250px" />

Custom header with standard method:
<pre>
val dialog = MonthYearPickerDialog.Builder(
	this,
	R.style.Style_MonthYearPickerDialog_Orange,
	{ year, month ->
		// do something
	},
	initialYear,
	initialMonth
)
	<b>.setNegativeButton("Return")
	.setPositiveButton(R.string.set)</b>
	.build()

<b>dialog.setCustomTitle(layoutInflater.inflate(R.layout.header, null))</b>
</pre>
<img src="https://github.com/Dzmitry-Lakisau/MonthYearPickerDialog/blob/develop/screenshots/Screenshot_02.jpg" width="250px" />

There are methods in the builder to set minimum month and year (default to January and 1970 respectively):
<pre>
MonthYearPickerDialog.Builder(
	this,
	R.style.Style_MonthYearPickerDialog_Orange,
	{ year, month ->
		// do something
	},
	2020,
	Calendar.JULY
)
	<b>.setMinMonth(Calendar.MAY)
	.setMinYear(2020)</b>
	.build()
</pre>
In example below this combination is set to May 2020. With years above minimum year it's possible to select all months including below minimum month. But if after this select 2020, selected month will change to May to set combination of minimum month and year.

<img src="https://github.com/Dzmitry-Lakisau/MonthYearPickerDialog/blob/develop/screenshots/Screenshot_03.jpg" width="246px" /> <img src="https://github.com/Dzmitry-Lakisau/MonthYearPickerDialog/blob/develop/screenshots/Screenshot_04.jpg" width="246px" /> <img src="https://github.com/Dzmitry-Lakisau/MonthYearPickerDialog/blob/develop/screenshots/Screenshot_05.jpg" width="246px" /> <img src="https://github.com/Dzmitry-Lakisau/MonthYearPickerDialog/blob/develop/screenshots/Screenshot_06.jpg" width="246px" />

There are methods in the builder to set maximum month and year (default to December and current year respectively):
<pre>
MonthYearPickerDialog.Builder(
	this,
	R.style.Style_MonthYearPickerDialog_Orange,
	{ year, month ->
		// do something
	},
	2022,
	Calendar.FEBRUARY
)
	<b>.setMaxMonth(Calendar.MARCH)
	.setMaxYear(2022)</b>
	.build()
</pre>
Same logic regarding limit is applied:

<img src="https://github.com/Dzmitry-Lakisau/MonthYearPickerDialog/blob/develop/screenshots/Screenshot_07.jpg" width="246px" /> <img src="https://github.com/Dzmitry-Lakisau/MonthYearPickerDialog/blob/develop/screenshots/Screenshot_08.jpg" width="246px" /> <img src="https://github.com/Dzmitry-Lakisau/MonthYearPickerDialog/blob/develop/screenshots/Screenshot_09.jpg" width="246px" /> <img src="https://github.com/Dzmitry-Lakisau/MonthYearPickerDialog/blob/develop/screenshots/Screenshot_10.jpg" width="246px" />

Limits can be combined. Example below shows how to let selection of month in range from May 2018 to February 2022.
<pre>
MonthYearPickerDialog.Builder(
	this,
	<b>R.style.Style_MonthYearPickerDialog_Red</b>,
	{ year, month ->
		// do something
	},
	2022,
	Calendar.FEBRUARY
)
	<b>.setMinMonth(Calendar.MAY)
	.setMinYear(2018)
	.setMaxMonth(Calendar.FEBRUARY)
	.setMaxYear(2022)</b>
	.build()
</pre>
<img src="https://github.com/Dzmitry-Lakisau/MonthYearPickerDialog/blob/develop/screenshots/Screenshot_11.jpg" width="246px" /> <img src="https://github.com/Dzmitry-Lakisau/MonthYearPickerDialog/blob/develop/screenshots/Screenshot_12.jpg" width="246px" /> <img src="https://github.com/Dzmitry-Lakisau/MonthYearPickerDialog/blob/develop/screenshots/Screenshot_13.jpg" width="246px" /> <img src="https://github.com/Dzmitry-Lakisau/MonthYearPickerDialog/blob/develop/screenshots/Screenshot_14.jpg" width="246px" /> <img src="https://github.com/Dzmitry-Lakisau/MonthYearPickerDialog/blob/develop/screenshots/Screenshot_15.jpg" width="246px" />

If you want to let selection of month from same range of months for every year, then there's special mode for this:
<pre>
MonthYearPickerDialog.Builder(
	this,
	R.style.Style_MonthYearPickerDialog_Red,
	{ year, month ->
		// do something
	},
	2022,
	Calendar.JULY
)
	<b>.setAnnualMode(true)</b>
	.setMinMonth(Calendar.MAY)
	.setMinYear(2018)
	.setMaxMonth(Calendar.AUGUST)
	.setMaxYear(2022)
	.build()
</pre>
It will drastically change behaviour of picker comparing to the previous example:

<img src="https://github.com/Dzmitry-Lakisau/MonthYearPickerDialog/blob/develop/screenshots/Screenshot_16.jpg" width="246px" /> <img src="https://github.com/Dzmitry-Lakisau/MonthYearPickerDialog/blob/develop/screenshots/Screenshot_17.jpg" width="246px" /> <img src="https://github.com/Dzmitry-Lakisau/MonthYearPickerDialog/blob/develop/screenshots/Screenshot_18.jpg" width="246px" />

There are mode for selection of month only and listener of when user has selected month (notice how label changes immediately after click on month without click on dialog's positive button). Also there is method to set month format:
<pre>
MonthYearPickerDialog.Builder(
	this,
	R.style.Style_MonthYearPickerDialog_Red,
	selectedMonth = Calendar.FEBRUARY
)
	.setMinMonth(Calendar.FEBRUARY)
	.setMaxMonth(Calendar.JULY)
	<b>.setMonthFormat("MMM")
	.setMode(MonthYearPickerDialog.Mode.MONTH_ONLY)
	.setOnMonthSelectedListener { month ->
		// do something
	}</b>
	.build()
</pre>

<img src="https://github.com/Dzmitry-Lakisau/MonthYearPickerDialog/blob/develop/screenshots/Screenshot_19.jpg" width="246px" /> <img src="https://github.com/Dzmitry-Lakisau/MonthYearPickerDialog/blob/develop/screenshots/Screenshot_20.jpg" width="246px" /> <img src="https://github.com/Dzmitry-Lakisau/MonthYearPickerDialog/blob/develop/screenshots/Screenshot_21.jpg" width="246px" />

There are mode for selection of year only and listener of when user has selected year:
<pre>
MonthYearPickerDialog.Builder(
	this,
	R.style.Style_MonthYearPickerDialog_Red,
	selectedYear = 2022
)
	.setMinYear(2010)
	<b>.setMode(MonthYearPickerDialog.Mode.YEAR_ONLY)
	.setOnYearSelectedListener { month ->
		// do something
	}</b>
	.build()
</pre>

<img src="https://github.com/Dzmitry-Lakisau/MonthYearPickerDialog/blob/develop/screenshots/Screenshot_22.jpg" width="246px" /> <img src="https://github.com/Dzmitry-Lakisau/MonthYearPickerDialog/blob/develop/screenshots/Screenshot_23.jpg" width="246px" /> 

With this you even can transform picker into any sequential number picker:

<img src="https://github.com/Dzmitry-Lakisau/MonthYearPickerDialog/blob/develop/screenshots/Screenshot_24.jpg" width="246px" />

## Styling

You can style your dialog with required parameter **themeResId** in the builder. Next attributes will help you to do this:

```
<attr name="headerBackgroundColor" format="color|reference" />
<attr name="headerTextColor" format="color|reference" />
<attr name="headerTextColorSelected" format="color|reference" />

<attr name="monthBackgroundColorSelected" format="color|reference" />
<attr name="monthTextColor" format="color|reference" />
<attr name="monthTextColorDisabled" format="color|reference" />
<attr name="monthTextColorSelected" format="color|reference" />

<attr name="yearTextColor" format="color|reference" />
<attr name="yearTextColorSelected" format="color|reference" />
```

## Documentation

[Html](https://dzmitry-lakisau.github.io/MonthYearPickerDialog/docs/html/index.html)

[Javadoc](https://dzmitry-lakisau.github.io/MonthYearPickerDialog/docs/javadoc/index.html)

## Installation

Library can be fetched from [MavenCentral](https://mvnrepository.com/artifact/io.github.dzmitry-lakisau/month-year-picker-dialog)

**Gradle**
```
implementation 'io.github.dzmitry-lakisau:month-year-picker-dialog:1.0.0'  
```

**Maven**
```xml  
<dependency>  
	<groupId>io.github.dzmitry-lakisau</groupId>  
	<artifactId>month-year-picker-dialog</artifactId>  
	<version>1.0.0</version>  
</dependency>  
```

## License
   
<pre>
Copyright © 2021 Dzmitry Lakisau

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
</pre>
