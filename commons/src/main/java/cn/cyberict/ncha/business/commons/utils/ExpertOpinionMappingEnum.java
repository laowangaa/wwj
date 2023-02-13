package cn.cyberict.ncha.business.commons.utils;

import java.util.HashMap;
import java.util.Map;

public enum ExpertOpinionMappingEnum {
    APPR_CROS_PROT_APPLY_EXPERT("原址保护措施", "ApprCrosProtApplyExpert", "56004", "原址保护措施专家评审意见表.docx"),
    APPR_WORK_OPERATION_APPLY_EXPERT("保护范围涉建", "ApprWorkOperationApplyExpert", "56005", "保护范围涉建专家评审意见表.docx"),
    APPR_ARCH_ISSUE_MAJOR_APPLY_EXPERT("考古重大事项", "ApprArchIssueMajorApplyExpert", "56008-a", "考古工作计划专家评审意见表.docx"),
    APPR_ARCH_CRINWATER_APPLY_EXPERT("水下考古作业", "ApprArchCrinwaterApplyExpert", "56009", "考古工作计划专家评审意见表.docx"),
    APPR_PROV_CRINST_MOVE_APPLY("省保单位迁拆", "ApprProvCrinstMoveApplyExpert", "56010", "省保单位迁拆专家评审意见表.docx"),
    APPR_STATE_KEY_PCRI_CCZ_PLAN_APPLY_EXPERT("建控地带涉建", "ApprStateKeyPcriCczPlanApplyExpert", "56012", "建控地带涉建专家评审意见表.docx"),

    APPR_NATION_COOPERATION_APPRAISE_APPLY_EXPERT("文物境外鉴定", "ApprNationCooperationAppraiseApplyExpert", "56013", "文物境外鉴定专家评审意见表.docx"),

    APPR_NATIONWIDE_PCRI_REPAIR_RESCUE_APPLY_EXPERT("抢救工程方案", "ApprNationwidePcriRepairRescueApplyExpert", "56014-a", "抢救工程方案专家评审意见表.docx"),
    APPR_STATE_PCRI_SPP_APPLY_STATE_EXPERT("三防方案（国家局）", "ApprStatePcriSppApplyStateExpert", "56014-b", "方案审批专家评审意见表.docx"),
    APPR_KEY_PCRI_PP_APPLY_STATE_EXPERT("保护规划方案", "ApprKeyPcriPpApplyStateExpert", "56022", "保护规划方案专家评审意见表.docx"),

    APPR_NATIONWIDE_PCRI_REPAIR_PROPOSAL_APPLY_PROV_EXPERT("已获批方案（省局）", "ApprNationwidePcriRepairProposalApplyProvExpert", "56014-d", "方案审批专家评审意见表.docx"),
    APPR_KEY_PCRI_PP_APPLY_PROV_EXPERT("保护规划（省局）", "ApprKeyPcriPpApplyProvExpert", "56022-b", "保护规划方案专家评审意见表.docx"),
    APPR_STATE_PCRI_SPP_APPLY_PROV_EXPERT("三防方案（省局）", "ApprStatePcriSppApplyProvExpert", "56014-3-b", "方案审批专家评审意见表.docx"),
    APPR_HCP_QUAL_APPL_APPLY_EXPERT("文保资质申请", "ApprHcpQualApplApplyExpert", "56011-a", "文保资质申请专家评审意见表.docx"),

    APPR_HCP_QUAL_CHANGE_APPLY_EXPERT("文保资质变更", "ApprHcpQualChangeApplyExpert", "56011-b", "文物保护工程方案专家评审意见表.docx"),

    APPR_MUSEUM_CR_REPAIR_APPLY_EXPERT("文物修复计划", "ApprMuseumCrRepairApplyExpert", "56015-a", "馆藏一级文物修复申请专家评审意见表.docx"),
    APPR_MUSEUM_CR_COPY_APPLY_EXPERT("文物复制方案", "ApprMuseumCrCopyApplyExpert", "56015-b", "馆藏一级文物复制申请专家评审意见表.docx"),
    APPR_MUSEUM_CR_RUBBING_APPLY_EXPERT("文物拓印方案", "ApprMuseumCrRubbingApplyExpert", "56015-c", "馆藏一级文物拓印申请专家评审意见表.docx"),
    APPR_CRO_EXHIBIT_APPLY_EXPERT("文物出境展览", "ApprCroExhibitApplyExpert", "56016", "中国博物馆展览协会交流专业委员会专家函审意见表.docx"),

    APPR_MUSEUM_FCC_SAMPLING_ANALYSIS_APPLY_EXPERT("一级藏品取样", "ApprMuseumFccSamplingAnalysisApplyExpert", "56019", "一级藏品取样分析许可专家评审意见表.docx"),

    APPR_ARCH_ISSUE_COMM_APPLY_EXPERT("考古一般事项", "ApprArchIssueCommApplyExpert", "56008-b", "考古一般事项专家评审意见表.docx"),

    INDUSTRY_SAFETY_EXPERT("安全防护", "IndustrySafetyExpert", "56025", "资质审批专家意见.docx"),
    INDUSTRY_FIX_PLAN_EXPERT("修缮计划", "IndustryFixPlanExpert", "56024", "资质审批专家意见.docx"),
    INDUSTRY_APPLY_APRCRPP_EXPERT("文物保护", "IndustryFixfdPlanExpert", "56023", "资质审批专家意见.docx");

    private String apprName;
    private String tableName;
    private String apprCode;
    private String templateFile;

    ExpertOpinionMappingEnum(String apprName, String tableName, String apprCode, String templateFile) {
        this.apprName = apprName;
        this.tableName = tableName;
        this.apprCode = apprCode;
        this.templateFile = templateFile;
    }

    public String getApprName() {
        return apprName;
    }

    public void setApprName(String apprName) {
        this.apprName = apprName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getApprCode() {
        return apprCode;
    }

    public void setApprCode(String apprCode) {
        this.apprCode = apprCode;
    }

    public String getTemplateFile() {
        return templateFile;
    }

    public void setTemplateFile(String templateFile) {
        this.templateFile = templateFile;
    }

    private final static Map<String, ExpertOpinionMappingEnum> map = new HashMap<>();

    static {
        for (ExpertOpinionMappingEnum expertOpinionMappingEnum : ExpertOpinionMappingEnum.values()) {
            map.put(expertOpinionMappingEnum.getApprCode(), expertOpinionMappingEnum);
            map.put(expertOpinionMappingEnum.getTableName(), expertOpinionMappingEnum);
        }
    }

    public static ExpertOpinionMappingEnum getExpertOpinionMappingEnum(String entityName) {
        return map.get(entityName);
    }
}
