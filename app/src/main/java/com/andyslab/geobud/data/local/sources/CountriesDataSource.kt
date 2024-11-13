package com.andyslab.geobud.data.local.sources

// hashSets are optimized for fast membership checks
class CountriesDataSource{
    val AS by lazy {
        hashSetOf("Afghanistan","Armenia","Azerbaijan","Bahrain","Bangladesh","Bhutan","Brunei",
        "Cambodia","China","India","Indonesia","Georgia","India","Indonesia","Iran","Iraq","Israel",
        "Japan","Jordan","Kazakhstan","Kuwait","Kyrgyzstan","Laos","Lebanon","Malaysia","Maldives","Mongolia","Myanmar",
        "Nepal","North Korea","Oman","Pakistan","Philippines","Qatar","Saudi Arabia","Singapore","South Korea",
        "Sri Lanka","Syria","Tajikistan","Thailand","Timor-Leste","Turkmenistan","United Arab Emirates",
        "Uzbekistan","Vietnam","Yemen")
    }

    val AF by lazy {
        hashSetOf("Algeria","Angola","Benin","Botswana","Burkina Faso","Burundi","Cape Verde","Cameroon",
        "Central African Republic","Chad","Comoros","Congo","Djibouti","Egypt","Equatorial Guinea","Eritrea",
        "Eswatini","Ethiopia","Gabon","Gambia","Ghana","Guinea","Guinea-Bissau","Ivory Coast","Kenya","Lesotho",
        "Liberia","Libya","Madagascar","Malawi","Mali","Mauritania","Mauritius","Morocco","Mozambique","Namibia",
        "Niger","Nigeria", "North Africa", "Rwanda","Sao Tome and Principe","Senegal","Seychelles","Sierra Leone","Somalia",
        "South Africa","South Sudan","Sudan","Tanzania","Togo","Tunisia","Uganda","Zambia","Zimbabwe")
    }

    val NA by lazy {
        hashSetOf("Antigua and Barbuda","Bahamas","Barbados","Belize","Canada","Costa Rica","Cuba",
            "Dominica","El Salvador","Grenada","Guatemala","Haiti","Honduras","Jamaica","Mexico","Nicaragua","Panama",
            "Saint Kitts and Nevis","Saint Lucia","Saint Vincent and the Grenadines","Trinidad and Tobago",
            "United States of America",)
    }

    val SA by lazy {
        hashSetOf("Argentina","Bolivia","Brazil","Chile","Colombia","Easter Island","Ecuador","Guyana","Paraguay",
            "Peru","Suriname","Uruguay","Venezuela")
    }

    val EU by lazy {
        hashSetOf("Albania","Andorra","Austria","Belarus","Belgium","Bosnia and Herzegovina","Bulgaria",
            "Croatia","Cyprus","Czech Republic","Denmark","Estonia","England","Finland","France","Germany","Greece","Hungary",
            "Iceland","Ireland","Italy","Latvia","Liechtenstein","Lithuania","Luxembourg","Malta","Moldova","Monaco",
            "Montenegro","Netherlands","North Macedonia","Norway","Poland","Portugal","Romania","Russia","San Marino",
            "Serbia","Slovakia","Spain","Sweden","Switzerland","Turkey","Ukraine","United Kingdom","Vatican City")
    }

    val OC by lazy {
        hashSetOf("Australia","Fiji","Kiribati","Marshall Islands","Micronesia","Nauru","New Zealand",
            "Palau","Papua New Guinea","Samoa","Solomon Islands","Tonga","Tuvalu","Vanuatu")
    }
}

