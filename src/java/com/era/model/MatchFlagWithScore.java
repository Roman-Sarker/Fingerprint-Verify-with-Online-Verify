
package com.era.model;

/**
 *
 * @author root
 */
public class MatchFlagWithScore {
    private Boolean matchFlag;
    private String matchScore ;
    private String userGivenMatchScore;
    private String fingerName;

    public String getFingerName() {
        return fingerName;
    }

    public void setFingerName(String fingerName) {
        this.fingerName = fingerName;
    }

    public String getUserGivenMatchScore() {
        return userGivenMatchScore;
    }

    public void setUserGivenMatchScore(String userGivenMatchScore) {
        this.userGivenMatchScore = userGivenMatchScore;
    }

    public Boolean getMatchFlag() {
        return matchFlag;
    }

    public void setMatchFlag(Boolean matchFlag) {
        this.matchFlag = matchFlag;
    }

    public String getMatchScore() {
        return matchScore;
    }

    public void setMatchScore(String matchResult) {
        this.matchScore = matchResult;
    }
    
}
