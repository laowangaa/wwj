package cn.cyberict.workflow.execute.service;


import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;

import java.util.Map;


public interface ActivitiService {

    
    String findProcessInstanceByProcessKey(String processKey);

    
    Task findTaskByProcInstId(String id);

    
    void setVariable(String id, Map map);

    
    void setVariableOfInt(String id, String variableName, int variableValue);

    
    void setVariableOfString(String id, String variableName, String variableValue);

    
    void accomplishActiviti(String id, Map map);

    
    void accomplishActivitiById(String id);


    
    void deleteProcessInstance(String procInstId);
}
