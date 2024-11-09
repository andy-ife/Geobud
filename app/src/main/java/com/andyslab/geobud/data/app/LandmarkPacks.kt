package com.andyslab.geobud.data.app

import com.andyslab.geobud.data.model.Landmark

sealed class LandmarkPacks {
    companion object{
        val LANDMARKS = listOf(

            Landmark("eiffel tower","France","EU",
                "Champ de Mars, Paris",
                "The Eiffel Tower has been repainted various different colors over the years" +
                    " including reddish-brown, bright yellow, and a special shade called 'Eiffel" +
                    " Tower Brown'."),

            Landmark("great wall of china", "China","AS",
                "Huairou District",
                "The construction of the Great Wall took over 2 thousand years!"),

            Landmark("kremlin","Russia","EU","Moscow",
            "The Kremlin is home to the world's largest bell AND the world's largest cannon!"),

            Landmark("leaning tower of pisa","Italy","EU", "Piazza del Duomo, Pisa",
                "The soft soil beneath Pisa Tower that causes its signature lean also prevents " +
                        "it from reverberating, and has helped it survive 4 earthquakes."),

            Landmark("great pyramid of giza","Egypt","AF","Giza",
                "The Great Pyramid of Giza is the 3rd heaviest man-made object in the world. " +
                        "The heaviest is the Great Wall of China, and the 2nd heaviest is the Three Gorges Dam in China"),

            Landmark("sydney opera house","Australia","OC", "Bennelong Point, Sydney",
                "233 designs were submitted for the Opera House international design " +
                        "competition held in 1956. Jørn Utzon from Denmark was announced the " +
                        "winner, receiving ₤5000 for his design."),

            Landmark("statue of liberty","United States of America","NA",
                "Liberty Island, New York City",
                "The Statue of Liberty is thought to have been struck up to 600 " +
                        "times by lightning."),


            Landmark("taj mahal","India","AS", "Agra, Uttar Pradesh",
                "The color of th Taj Mahal keeps changing throughout the day as the sun rises and " +
                        "falls. It appears pink in the morning, white at noon, and golden in" +
                        "moonlight."),

            Landmark("moai","Easter Island","SA","Eastern Polynesia",
            "It is still unknown how the Rapa Nui people managed the remarkable " +
                    "feat of moving the 14 ton statues across the island. Sometimes over " +
                    "several kilometers."),

            Landmark("machu picchu","Peru","SA", "Andes Mountains",
                "The Incas used very little mortar in Machu Picchu construction" +
                        ". They used a special technique called ashlar, which requires " +
                        "stones cut so precisely that there is virtually no space between joints."),


                Landmark("christ the redeemer","Brazil","SA"),
                Landmark("colosseum","Italy","EU"),
                Landmark("big ben","England","EU"),
                Landmark("la sagrada familia","Spain","EU"),
                Landmark("golden gate bridge","United States of America","NA"),
                Landmark("trevi fountain","Italy","EU"),
                Landmark("brandenburg gate","Germany","EU"),
                Landmark("seattle space needle","United States of America","NA"),
                Landmark("acropolis","Greece","EU"),
                Landmark("burj khalifa","United Arab Emirates","AS"),
                Landmark("the louvre","France","EU"),
                Landmark("mount rushmore","United States of America","NA"),
                Landmark("uluru","Australia","OC"),
                Landmark("stonehenge","England","EU"),
                Landmark("mount kilimanjaro","Tanzania","AF"),


                Landmark("moai","Easter Island","SA"),
                Landmark("machu picchu","Peru","SA"),
                Landmark("alhambra","Spain","EU"),
                Landmark("bondi beach","Australia","OC"),
                Landmark("buckingham palace","England","EU"),
                Landmark("cn tower","Canada","NA"),
                Landmark("washington monument","United States of America","NA"),
                Landmark("great barrier reef","Australia","OC"),
                Landmark("notre dame","France","EU"),
                Landmark("niagara falls","Canada","NA"),
                Landmark("mount everest","Nepal","AS"),
                Landmark("pompeii","Italy","EU"),
                Landmark("angkor wat","Cambodia","AS"),
                Landmark("burj khalifa","United Arab Emirates","AS"),
                Landmark("saint marks basilica","Italy","EU"),
                Landmark("avenue of baobabs","Madagascar","Africa"),
                Landmark("great wall of china", "China","AS"),
                Landmark("aloba arch","Chad","AF"),
                Landmark("antelope canyon","United States of America","NA"),
                Landmark("table mountain","South Africa","AF"),


                Landmark("st pauls cathedral","England","EU",),
                Landmark("palace of the versailles","France","EU"),
                Landmark("chichen itza","Mexico","NA",),
                Landmark("taj mahal","India","AS"),
                Landmark("colosseum","Italy","EU"),
                Landmark("big ben","England","EU"),
                Landmark("buckingham palace","England","EU"),
                Landmark("mount fuji","Japan","AS"),
                Landmark("the london eye","England","EU",),
                Landmark("neuschwanstein castle","Germany","EU",),
                Landmark("christ the redeemer","Brazil","SA"),
                Landmark("tapei 101","Taiwan","AS"),
                Landmark("banff national park","Canada","NA"),
                Landmark("spanish steps","Italy","EU"),
                Landmark("marina bay sands","Singapore","AS"),
                Landmark("shwedagon pagoda","Myanmar","AS"),
                Landmark("the shard","England","EU"),
                Landmark("seoul tower","South Korea","AS"),
                Landmark("potala palace","China","AS"),
                Landmark("the terracotta army museum","China","AS"),
                Landmark("great pyramid of cholula","Mexico","NA"),
                Landmark("itsukushima shrine","Japan","AS"),
                Landmark("giants causeway","Ireland","EU"),
                Landmark("dom luis bridge","Portugal","EU"),
                Landmark("tulum ruins","Mexico","SA"),
                Landmark("victoria falls","Zimbabwe","AF"),
                Landmark("sahara desert","North Africa","AF"))

    }
}