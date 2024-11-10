package com.andyslab.geobud.data.app

import com.andyslab.geobud.data.model.LandmarkModel

sealed class LandmarksPack {
    companion object{
        val LANDMARKS_PACK = listOf(

            LandmarkModel("eiffel tower","France","EU",
                "Champ de Mars, Paris",
                "The Eiffel Tower has been repainted various different colors over the years" +
                    " including reddish-brown, bright yellow, and a special shade called 'Eiffel" +
                    " Tower Brown'."),

            LandmarkModel("great wall of china", "China","AS",
                "Huairou District",
                "The construction of the Great Wall took over 2 thousand years!"),

            LandmarkModel("kremlin","Russia","EU","Moscow",
            "The Kremlin is home to the world's largest bell AND the world's largest cannon!"),

            LandmarkModel("leaning tower of pisa","Italy","EU", "Piazza del Duomo, Pisa",
                "The soft soil beneath Pisa Tower that causes its signature lean also prevents " +
                        "it from reverberating, and has helped it survive 4 earthquakes."),

            LandmarkModel("great pyramid of giza","Egypt","AF","Giza",
                "The Great Pyramid of Giza is the 3rd heaviest man-made object in the world. " +
                        "The heaviest is the Great Wall of China, and the 2nd heaviest is the Three Gorges Dam in China"),

            LandmarkModel("sydney opera house","Australia","OC", "Bennelong Point, Sydney",
                "233 designs were submitted for the Opera House international design " +
                        "competition held in 1956. Jørn Utzon from Denmark was announced the " +
                        "winner, receiving ₤5000 for his design."),

            LandmarkModel("statue of liberty","United States of America","NA",
                "Liberty Island, New York City",
                "The Statue of Liberty is thought to have been struck up to 600 " +
                        "times by lightning."),


            LandmarkModel("taj mahal","India","AS", "Agra, Uttar Pradesh",
                "The color of th Taj Mahal keeps changing throughout the day as the sun rises and " +
                        "falls. It appears pink in the morning, white at noon, and golden in" +
                        "moonlight."),

            LandmarkModel("moai","Easter Island","SA","Eastern Polynesia",
            "It is still unknown how the Rapa Nui people managed the remarkable " +
                    "feat of moving the 14 ton statues across the island. Sometimes over " +
                    "several kilometers."),

            LandmarkModel("machu picchu","Peru","SA", "Andes Mountains",
                "The Incas used very little mortar in Machu Picchu construction" +
                        ". They used a special technique called ashlar, which requires " +
                        "stones cut so precisely that there is virtually no space between joints."),


                LandmarkModel("christ the redeemer","Brazil","SA"),
                LandmarkModel("colosseum","Italy","EU"),
                LandmarkModel("big ben","England","EU"),
                LandmarkModel("la sagrada familia","Spain","EU"),
                LandmarkModel("golden gate bridge","United States of America","NA"),
                LandmarkModel("trevi fountain","Italy","EU"),
                LandmarkModel("brandenburg gate","Germany","EU"),
                LandmarkModel("seattle space needle","United States of America","NA"),
                LandmarkModel("acropolis","Greece","EU"),
                LandmarkModel("burj khalifa","United Arab Emirates","AS"),
                LandmarkModel("the louvre","France","EU"),
                LandmarkModel("mount rushmore","United States of America","NA"),
                LandmarkModel("uluru","Australia","OC"),
                LandmarkModel("stonehenge","England","EU"),
                LandmarkModel("mount kilimanjaro","Tanzania","AF"),


                LandmarkModel("moai","Easter Island","SA"),
                LandmarkModel("machu picchu","Peru","SA"),
                LandmarkModel("alhambra","Spain","EU"),
                LandmarkModel("bondi beach","Australia","OC"),
                LandmarkModel("buckingham palace","England","EU"),
                LandmarkModel("cn tower","Canada","NA"),
                LandmarkModel("washington monument","United States of America","NA"),
                LandmarkModel("great barrier reef","Australia","OC"),
                LandmarkModel("notre dame","France","EU"),
                LandmarkModel("niagara falls","Canada","NA"),
                LandmarkModel("mount everest","Nepal","AS"),
                LandmarkModel("pompeii","Italy","EU"),
                LandmarkModel("angkor wat","Cambodia","AS"),
                LandmarkModel("burj khalifa","United Arab Emirates","AS"),
                LandmarkModel("saint marks basilica","Italy","EU"),
                LandmarkModel("avenue of baobabs","Madagascar","Africa"),
                LandmarkModel("great wall of china", "China","AS"),
                LandmarkModel("aloba arch","Chad","AF"),
                LandmarkModel("antelope canyon","United States of America","NA"),
                LandmarkModel("table mountain","South Africa","AF"),


                LandmarkModel("st pauls cathedral","England","EU",),
                LandmarkModel("palace of the versailles","France","EU"),
                LandmarkModel("chichen itza","Mexico","NA",),
                LandmarkModel("taj mahal","India","AS"),
                LandmarkModel("colosseum","Italy","EU"),
                LandmarkModel("big ben","England","EU"),
                LandmarkModel("buckingham palace","England","EU"),
                LandmarkModel("mount fuji","Japan","AS"),
                LandmarkModel("the london eye","England","EU",),
                LandmarkModel("neuschwanstein castle","Germany","EU",),
                LandmarkModel("christ the redeemer","Brazil","SA"),
                LandmarkModel("tapei 101","Taiwan","AS"),
                LandmarkModel("banff national park","Canada","NA"),
                LandmarkModel("spanish steps","Italy","EU"),
                LandmarkModel("marina bay sands","Singapore","AS"),
                LandmarkModel("shwedagon pagoda","Myanmar","AS"),
                LandmarkModel("the shard","England","EU"),
                LandmarkModel("seoul tower","South Korea","AS"),
                LandmarkModel("potala palace","China","AS"),
                LandmarkModel("the terracotta army museum","China","AS"),
                LandmarkModel("great pyramid of cholula","Mexico","NA"),
                LandmarkModel("itsukushima shrine","Japan","AS"),
                LandmarkModel("giants causeway","Ireland","EU"),
                LandmarkModel("dom luis bridge","Portugal","EU"),
                LandmarkModel("tulum ruins","Mexico","SA"),
                LandmarkModel("victoria falls","Zimbabwe","AF"),
                LandmarkModel("sahara desert","North Africa","AF"))

    }
}