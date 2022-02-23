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
interface ICompareStrategy :ICompare{

    fun compare(targetTask: BaseTask, runningTask: BaseTask): ComparedResultType

}