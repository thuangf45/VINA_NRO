package com.girlkun.models.skill;

import com.girlkun.models.Template.SkillTemplate;
import java.util.ArrayList;
import java.util.List;
import com.girlkun.utils.Util;

/**
 * Lớp đại diện cho một lớp nhân vật trong game, quản lý thông tin và các kỹ năng của lớp nhân vật.
 * @author Lucifer
 */
public class NClass {

    /** ID của lớp nhân vật. */
    public int classId;

    /** Tên của lớp nhân vật. */
    public String name;

    /** Danh sách các mẫu kỹ năng của lớp nhân vật. */
    public List<SkillTemplate> skillTemplatess = new ArrayList<>();

    /**
     * Lấy mẫu kỹ năng dựa trên ID kỹ năng.
     *
     * @param tempId ID của kỹ năng cần tìm.
     * @return Mẫu kỹ năng tương ứng, hoặc null nếu không tìm thấy.
     */
    public SkillTemplate getSkillTemplate(int tempId) {
        for (SkillTemplate skillTemplate : skillTemplatess) {
            if (skillTemplate.id == tempId) {
                return skillTemplate;
            }
        }
        return null;
    }

    /**
     * Lấy mẫu kỹ năng dựa trên tên kỹ năng, không phân biệt dấu.
     *
     * @param name Tên kỹ năng cần tìm.
     * @return Mẫu kỹ năng tương ứng, hoặc null nếu không tìm thấy.
     */
    public SkillTemplate getSkillTemplateByName(String name) {
        for (SkillTemplate skillTemplate : skillTemplatess) {
            if ((Util.removeAccent(skillTemplate.name).toUpperCase()).contains(Util.removeAccent(name).toUpperCase())) {
                return skillTemplate;
            }
        }
        return null;
    }
}