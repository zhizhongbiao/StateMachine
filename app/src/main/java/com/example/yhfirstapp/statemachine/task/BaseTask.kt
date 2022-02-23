package com.example.yhfirstapp.statemachine.task

import android.util.Log
import androidx.annotation.CallSuper
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


/**
 *
 * @ProjectName: YhFirstApp
 * @Package: com.example.yhfirstapp.statemachine.check
 * @Author: zhizb
 * @Description: java类作用描述
 * @CreateDate: 2022/2/23
 * @UpdateUser: zhizb
 * @UpdateDate: 2022/2/23
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */

abstract class BaseTask(val id: Long, val type: TaskType) : ITask {

    val onTaskState = MutableStateFlow(TaskState.INIT)
    private val cs = CoroutineScope(Dispatchers.IO)


    init {
        cs.launch {
            onTaskState.collect {
                onState(it)
            }
        }

    }

    @CallSuper
    override fun onReject(reason: String) {
        Log.e("Zzb", "onReject  reason: $reason")
        cs.launch { onTaskState.emit(TaskState.REJECTED) }
    }

    @CallSuper
    override fun onStart() {
        cs.launch { onTaskState.emit(TaskState.RUNNING) }

    }

    @CallSuper
    override fun onResume() {
        cs.launch { onTaskState.emit(TaskState.RUNNING) }


    }

    @CallSuper
    override fun onPause() {
        cs.launch { onTaskState.emit(TaskState.SUSPENDED) }

    }

    @CallSuper
    override fun onDestroy() {
        cs.launch { onTaskState.emit(TaskState.DESTROYED) }
        finish()
    }

    @CallSuper
    override fun finish() {
        cs.launch { onTaskState.emit(TaskState.FINISHED) }
        cs.cancel()
    }
}