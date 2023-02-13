package cn.cyberict.workflow.execute.service.impl;

import cn.cyberict.workflow.execute.service.ActivitiService;
import com.codingapi.txlcn.tc.annotation.DTXPropagation;
import com.codingapi.txlcn.tc.annotation.TxcTransaction;
import org.activiti.engine.ActivitiObjectNotFoundException;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
import java.util.Map;


@Service
@Transactional
public class ActivitiServiceImpl implements ActivitiService {

    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;

    @Override
    @TxcTransaction(propagation = DTXPropagation.REQUIRED)
    public String findProcessInstanceByProcessKey(String processKey) {
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processKey);
        return processInstance.getId();
    }

    @Override
    public Task findTaskByProcInstId(String id) {
        Task task = taskService.createTaskQuery().processInstanceId(id).active().singleResult();
        return task;
    }

    @Override
    public void setVariable(String id, Map map) {
        if (map != null) {
            for (Object key : map.keySet()) {
                taskService.setVariable(id, (String) key, map.get(key));
            }
        }
    }

    @Override
    public void setVariableOfInt(String id, String variableName, int variableValue) {
        taskService.setVariable(id, variableName, variableValue);
    }

    @Override
    public void setVariableOfString(String id, String variableName, String variableValue) {
        taskService.setVariable(id, variableName, variableValue);
    }

    @Override
    public void accomplishActiviti(String id, Map map) {
        if (map != null) {
            for (Object key : map.keySet()) {
                taskService.setVariable(id, (String) key, map.get(key));
            }
        }
        taskService.complete(id, map);
    }

    @Override
    public void accomplishActivitiById(String id) {
        taskService.complete(id);
    }

    @Override
    public void deleteProcessInstance(String procInstId) {
        try {
            runtimeService.deleteProcessInstance(procInstId, "结束流程");
        } catch (ActivitiObjectNotFoundException e) {
            System.out.println("流程已结束");
        }

    }
}
