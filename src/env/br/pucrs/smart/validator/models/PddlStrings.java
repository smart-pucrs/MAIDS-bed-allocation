package br.pucrs.smart.validator.models;

public class PddlStrings {
	
	private String domain;
	private String problem;
	private String plan;
	
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	public String getProblem() {
		return problem;
	}
	public void setProblem(String problem) {
		this.problem = problem;
	}
	public String getPlan() {
		return plan;
	}
	public void setPlan(String plan) {
		this.plan = plan;
	}
	
	@Override
	public String toString() {
	    StringBuilder sb = new StringBuilder();
	    sb.append("{ ");
        if (domain != null) {
        	sb.append(" domain : ");
        	sb.append(domain);
        }
        if (problem != null) {
        	sb.append(" problem : ");
        	sb.append(problem);
        }
        if (plan != null) {
        	sb.append(" plan: ");
        	sb.append(plan);
        }
        sb.append("} ");
		return sb.toString();
	}

}
