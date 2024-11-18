package com.andyslab.geobud.domain

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

class StartTimerLoopUseCase {
    companion object{
        val millisLeft = MutableSharedFlow<Long>()
        var job = Job()
            get() {
                if(field.isCancelled) field = Job()
                return field
            }
    }

    operator fun invoke(millisTillNextHeart: Long?){
        job.cancel()
        CoroutineScope(Dispatchers.Default).launch(job){
            var m = millisTillNextHeart ?: return@launch
            while(true){
                m-=1000
                delay(1000)
                millisLeft.emit(m)
                if(m <= 0L) m = 600000
            }
        }
    }
}