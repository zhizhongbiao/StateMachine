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

class BasicModuleManager private constructor() :IModuleManager{


    companion object {
        val inst = BasicModuleManager()
    }

    override fun init() {
        TODO("Not yet implemented")
    }

    override fun assessModule(module: IBasicModule) {
        TODO("Not yet implemented")
    }


}



