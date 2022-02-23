package com.example.yhfirstapp.statemachine.strategy

import com.example.yhfirstapp.statemachine.task.*


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
interface IMachineManager {

    fun init()

    fun startTask(targetTask:BaseTask,strategy: ICompareStrategy):IMachineManager

    fun deployStrategy(strategy: ICompareStrategy,targetTask: BaseTask, runningTask: BaseTask): ComparedResultType

    suspend  fun switchTask(result: ComparedResult, targetTask: BaseTask, runningTask: BaseTask?)

    fun retrieveMachineState(): MachineState
    fun retrieveCurrentTaskType(): TaskType

    fun addCallback(cb: MachineCallback)
    fun removeCallback(cb: MachineCallback)

    fun destroy()

}


interface MachineCallback{

    fun onState(state: MachineState)

}