package com.example.yhfirstapp.statemachine.check

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
interface IBasicModule:IIOModule,IMainModule,INetModule,IPowerModule,ISenseModule{

    fun allModuleReady():Boolean

    fun allModuleInfo():String
}