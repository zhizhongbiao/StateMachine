package com.example.yhfirstapp.statemachine.task


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
data class ComparedResult(val result: ComparedResultType,
                          val targetTask:BaseTask,
                          val runningTask:BaseTask?){
    override fun toString(): String {
        return "ComparedResult(result=$result, " +
                "targetTask='${targetTask.info()}'," +
                "runningTask='${runningTask?.info()}')"
    }
}

enum class ComparedResultType{
    NOTHING,PARALLEL,BREAK,PAUSE
}