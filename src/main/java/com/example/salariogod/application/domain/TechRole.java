package com.example.salariogod.application.domain;

import com.example.salariogod.persistence.PersistableEnum;

public enum TechRole implements PersistableEnum<Short> {
    SOFTWARE_ARCHITECT(0),
    TECH_LEAD(1),
    FULLSTACK(2),
    FRONTEND(3),
    BACKEND(4),
    DBA(5),
    DEVOPS(6),
    SRE(7),
    INFOSEC(8),
    UX_UI(9),
    FUNCTIONAL_ANALYST(10),
    DATA_SCIENTIST(11),
    DATA_ANALYST(12),
    QA_TESTER(13),
    QA_AUTOMATION(14),
    HELPDESK(15),
    TECHSUPPORT(16),
    SCRUM_MASTER(17),
    HR_RECRUITER(18),
    PRODUCT_MANAGER(19),
    MANAGER_DIRECTOR(20),
    CONSULTANT(21),
    FINANCE(22),
    SALES(23),
    OTHER(24);

    private final short value;

    TechRole(int value) {
        this.value = (short) value;
    }

    @Override
    public Short getValue() {
        return this.value;
    }
}
