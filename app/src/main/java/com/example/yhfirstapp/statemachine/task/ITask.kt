package com.example.yhfirstapp.statemachine.task

import com.example.yhfirstapp.statemachine.check.IBasicModule

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

interface ITask :ICompare{

    fun onReject(reason:String)
    fun onStart()
    fun onResume()
    fun onPause()
    fun onDestroy()
    fun finish()

    fun onState(state: TaskState)

    fun retrieveBaseRequirement(): IBasicModule

    fun retrieveSubTask(): BaseTask

    fun onStep(step: Int)//使用责任链模式+泛型暴露出去

    fun info(): String

}


enum class TaskState {
    INIT,REJECTED,RUNNING, SUSPENDED, DESTROYED,FINISHED
}
