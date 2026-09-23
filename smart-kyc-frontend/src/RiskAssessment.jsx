import { useEffect, useState } from "react";
import "./Customers.css";

function RiskAssessment({ setPage }) {

    console.log("Risk Assessment page loaded");

    const [riskData, setRiskData] = useState([]);

    useEffect(() => {

        fetch("http://localhost:8080/api/risk")
            .then((response) => response.json())
            .then((data) => {
                setRiskData(data);
            })
            .catch((error) => {
                console.error("Error fetching risk data:", error);
            });

    }, []);

    return (
        <div className="dashboard">

            {/* Sidebar */}
            <aside className="sidebar">

                <h2>Smart KYC</h2>

                <p className="subtitle">
                    Risk Management
                </p>

                <nav>

                    <a onClick={() => setPage("dashboard")}>
                        Dashboard
                    </a>

                    <a onClick={() => setPage("customers")}>
                        Customers
                    </a>

                    <a onClick={() => setPage("kyc")}>
                        KYC Verification
                    </a>

                    <a className="active">
                        Risk Assessment
                    </a>

                    <a onClick={() => setPage("pep")}>
    PEP Screening
</a>

                    <a onClick={() => setPage("audit")}>
    Audit Logs
</a>

                </nav>

            </aside>

            {/* Main Content */}
            <main className="main-content">

                <div className="customers-page">

                    {/* Header */}
                    <div className="customers-header">

                        <div>

                            <h1>
                                Risk Assessment
                            </h1>

                            <p>
                                Monitor customer risk levels and assessment results
                            </p>

                        </div>

                    </div>

                    {/* Risk Summary Cards */}
                    <div className="risk-summary">

                        <div className="risk-card">

                            <span>
                                Total Assessments
                            </span>

                            <strong>
                                {riskData.length}
                            </strong>

                        </div>

                        <div className="risk-card">

                            <span>
                                Low Risk
                            </span>

                            <strong>
                                {
                                    riskData.filter(
                                        risk => risk.riskLevel === "LOW"
                                    ).length
                                }
                            </strong>

                        </div>

                        <div className="risk-card">

                            <span>
                                Medium Risk
                            </span>

                            <strong>
                                {
                                    riskData.filter(
                                        risk => risk.riskLevel === "MEDIUM"
                                    ).length
                                }
                            </strong>

                        </div>

                        <div className="risk-card">

                            <span>
                                High Risk
                            </span>

                            <strong>
                                {
                                    riskData.filter(
                                        risk => risk.riskLevel === "HIGH"
                                    ).length
                                }
                            </strong>

                        </div>

                    </div>

                    {/* Risk Table */}
                    <div className="customers-card">

                        <div className="table-header">

                            <h3>
                                Risk Assessments
                            </h3>

                            <span>
                                {riskData.length} Assessments
                            </span>

                        </div>

                        <div className="table-responsive">

                            <table className="customers-table">

                                <thead>

                                    <tr>

                                        <th>ID</th>

                                        <th>
                                            Customer Name
                                        </th>

                                        <th>
                                            Risk Score
                                        </th>

                                        <th>
                                            Risk Level
                                        </th>

                                        <th>
                                            Risk Reason
                                        </th>

                                        <th>
                                            KYC Status
                                        </th>

                                        <th>
                                            Assessed At
                                        </th>

                                    </tr>

                                </thead>

                                <tbody>

                                    {riskData.map((risk) => (

                                        <tr key={risk.id}>

                                            <td>
                                                #{risk.id}
                                            </td>

                                            <td>

                                                <strong>
                                                    {risk.customer?.fullName || "N/A"}
                                                </strong>

                                            </td>

                                            <td>

                                                <span
                                                    className={`risk-score ${
                                                        risk.riskScore <= 30
                                                            ? "low"
                                                            : risk.riskScore <= 60
                                                            ? "medium"
                                                            : "high"
                                                    }`}
                                                >
                                                    {risk.riskScore} / 100
                                                </span>

                                            </td>

                                            <td>

                                                <span
                                                    className={`risk-badge ${
                                                        risk.riskLevel?.toLowerCase()
                                                    }`}
                                                >
                                                    {risk.riskLevel}
                                                </span>

                                            </td>

                                            <td>

                                                <span className="risk-reason">
                                                    {risk.riskReason || "No reason"}
                                                </span>

                                            </td>

                                            <td>

                                                <span
                                                    className={`status-badge ${
                                                        risk.customer?.kycStatus?.toLowerCase()
                                                    }`}
                                                >
                                                    {risk.customer?.kycStatus || "N/A"}
                                                </span>

                                            </td>

                                            <td>

                                                {risk.assessedAt
                                                    ? new Date(
                                                        risk.assessedAt
                                                    ).toLocaleDateString(
                                                        "en-GB",
                                                        {
                                                            day: "2-digit",
                                                            month: "short",
                                                            year: "numeric"
                                                        }
                                                    )
                                                    : "N/A"}

                                            </td>

                                        </tr>

                                    ))}

                                </tbody>

                            </table>

                        </div>

                    </div>

                </div>

            </main>

        </div>
    );
}

export default RiskAssessment;