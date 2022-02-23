package com.example.yhfirstapp.statemachine.strategy

import com.example.yhfirstapp.statemachine.task.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.*
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
class MachineManager private constructor() : IMachineManager {

    private var runningTask: BaseTask? = null
    private var suspendedTask: BaseTask? = null
    private val cs = CoroutineScope(Dispatchers.IO)

    val machineState = MutableStateFlow(MachineState.IDLE)

    val currentTaskType = MutableStateFlow(TaskType.NON)


    companion object {
        val inst = MachineManager()
    }


    init {
        cs.launch {
            currentTaskType.collect {
                machineState.emit(if (it == TaskType.NON) MachineState.IDLE else MachineState.WORKING)
            }
        }
    }

    private val listeners by lazy {
        mutableListOf<MachineCallback>()
    }

    override fun init() {
        TODO("Not yet implemented")
    }

    fun startTask(t: BaseTask) = startTask(t, strategy = DefaultCompareStrategy())

    override fun startTask(targetTask: BaseTask, strategy: ICompareStrategy) = this.apply {
        cs.launch {
            val rTask = runningTask
            val br = targetTask.retrieveBaseRequirement()
            when {
                machineState.value == MachineState.IDLE || !hasRunningTask() -> {
                    if (!br.allModuleReady()) {
                        targetTask.onReject(br.allModuleInfo())
                        return@launch
                    }
                    switchTask(
                        ComparedResult(ComparedResultType.BREAK, targetTask, null),
                        targetTask,
                        rTask
                    )
                }
                else -> {
                    if (!br.allModuleReady()) {
                        targetTask.onReject(br.allModuleInfo())
                        return@launch
                    }
                    val result = deployStrategy(strategy, targetTask, rTask!!)
                    switchTask(ComparedResult(result, targetTask, rTask), targetTask, rTask)

                }
            }
        }
    }

    override fun deployStrategy(
        strategy: ICompareStrategy,
        targetTask: BaseTask,
        rTask: BaseTask
    ) = strategy.compare(targetTask, rTask).also {
        val s = ComparedResult(it, targetTask, rTask)
        strategy.onComparedResult(s)
        targetTask.onComparedResult(s)
    }


    override suspend fun switchTask(
        result: ComparedResult,
        targetTask: BaseTask,
        rTask: BaseTask?
    ) {
        when (result.result) {
            ComparedResultType.BREAK -> {
                rTask?.onDestroy()
                updateRunningTask(targetTask)
                targetTask.onStart()
            }
            ComparedResultType.PARALLEL -> {
                suspendedTask = null
                targetTask.onStart()
                updateRunningTask(targetTask)
            }
            ComparedResultType.PAUSE -> {
                rTask?.onPause()
                suspendedTask = rTask
                updateRunningTask(targetTask)
                runningTask?.onTaskState?.collect {
                    if (it == TaskState.FINISHED) {
                        updateRunningTask(suspendedTask)
                        runningTask?.onResume()
                        suspendedTask = null
                    }
                }
                targetTask.onStart()
            }
            ComparedResultType.NOTHING -> targetTask.onReject(result.toString())
        }

    }

    private suspend fun updateRunningTask(t: BaseTask?) {
        runningTask = t
        runningTask?.onTaskState?.collect {
            when (it) {
                TaskState.RUNNING -> currentTaskType.emit(runningTask!!.type)
                TaskState.FINISHED -> {
                    if (!hasSuspendedTask()) runningTask = null
                    currentTaskType.emit(TaskType.NON)
                }
            }
        }
    }

    override fun retrieveMachineState() = machineState.value!!

    override fun retrieveCurrentTaskType() = currentTaskType.value!!

    override fun addCallback(cb: MachineCallback) {
        removeCallback(cb)
        listeners.add(cb)
    }

    override fun removeCallback(cb: MachineCallback) {
        listeners.remove(cb)
    }

    fun hasSuspendedTask() = suspendedTask != null
    fun hasRunningTask() = runningTask != null

    override fun destroy() {
        suspendedTask = null
        runningTask = null
        listeners.clear()
        cs.cancel()
    }

}