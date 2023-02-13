package cn.cyberict.workflow.execute.controller;

import cn.cyberict.workflow.execute.service.ActivitiService;
import com.alibaba.fastjson.JSONObject;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
public class ActivitiController {

    @Autowired
    ActivitiService activitiService;
    
    @RequestMapping(value = "/findProcessInstanceByProcessKey")
    public String findProcessInstanceByProcessKey(String processKey){
        return activitiService.findProcessInstanceByProcessKey(processKey);
    }

    
    @RequestMapping(value = "/findTaskByProcInstId")
    public Map findTaskByProcInstId(String id) {
        Task task = activitiService.findTaskByProcInstId(id);
        Map map = new HashMap();
        map.put("id",task.getId());
        map.put("taskDefinitionKey",task.getTaskDefinitionKey());
        return map;
    }

    
    @RequestMapping(value = "/setVariable", method = RequestMethod.POST)
    public void setVariable(String id, String json) {
        Map map = JSONObject.parseObject(json,Map.class);
        activitiService.setVariable(id, map);
    }

    
    @RequestMapping(value = "/setVariableOfInt", method = RequestMethod.POST)
    public void setVariableOfInt(String id, String variableName, int variableValue) {
        activitiService.setVariableOfInt(id,variableName,variableValue);
    }
    
    @RequestMapping(value = "/setVariableOfString", method = RequestMethod.POST)
    public void setVariableOfString(String id, String variableName, String variableValue) {
        activitiService.setVariableOfString(id,variableName,variableValue);
    }

    
    @RequestMapping(value = "/accomplishActiviti", method = RequestMethod.POST)
    public void accomplishActiviti(String id, String json) {
        Map map = JSONObject.parseObject(json,Map.class);
        activitiService.accomplishActiviti(id, map);
    }

    
    @RequestMapping(value = "/accomplishActivitiById")
    public void accomplishActivitiById(String id) {
        activitiService.accomplishActivitiById(id);
    }


    
    @RequestMapping(value = "/deleteProcessInstance")
    public void deleteProcessInstance(String procInstId) {
        activitiService.deleteProcessInstance(procInstId);
    }

    @RequestMapping(value = "hi")
    public String hi(){
        return "workflow" + new Date();
    }

}
