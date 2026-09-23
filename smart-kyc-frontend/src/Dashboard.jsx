import { useEffect, useState } from "react";
import "./Dashboard.css";

function Dashboard({ setPage }) {

    const [dashboardData, setDashboardData] = useState(null);
    const [auditLogs, setAuditLogs] = useState([]);

    const fetchDashboardData = () => {

        fetch("http://localhost:8080/api/dashboard")
            .then((response) => {

                if (!response.ok) {
                    throw new Error("Failed to fetch dashboard data");
                }

                return response.json();
            })
            .then((data) => {
                setDashboardData(data);
            })
            .catch((error) => {
                console.error("Error fetching dashboard data:", error);
            });
    };

    const fetchAuditLogs = () => {

        fetch("http://localhost:8080/api/audit-logs")
            .then((response) => {

                if (!response.ok) {
                    throw new Error("Failed to fetch audit logs");
                }

                return response.json();
            })
            .then((data) => {

                const latestLogs = [...data].reverse();

                setAuditLogs(latestLogs);
            })
            .catch((error) => {
                console.error("Error fetching audit logs:", error);
            });
    };

    useEffect(() => {

        // Initial load
        fetchDashboardData();
        fetchAuditLogs();

        // Auto refresh every 10 seconds
        const refreshInterval = setInterval(() => {

            fetchDashboardData();
            fetchAuditLogs();

        }, 10000);

        // Stop interval when leaving Dashboard
        return () => {
            clearInterval(refreshInterval);
        };

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

                    <a className="active">
                        Dashboard
                    </a>

                    <a onClick={() => setPage("customers")}>
                        Customers
                    </a>

                    <a onClick={() => setPage("kyc")}>
                        KYC Verification
                    </a>

                    <a onClick={() => setPage("risk")}>
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

                {/* Header */}

                <div className="top-section">

                    <div>

                        <h1>
                            Dashboard
                        </h1>

                        <p>
                            KYC Verification & Risk Management System
                        </p>

                    </div>

                    <div className="admin">
                        Admin
                    </div>

                </div>

                {/* Summary Cards */}

                <div className="cards">

                    <div className="card total-card">

                        <div className="card-icon">
                            👥
                        </div>

                        <span>
                            Total Customers
                        </span>

                        <h2>
                            {dashboardData?.totalCustomers ?? 0}
                        </h2>

                    </div>


                    <div className="card pending-card">

                        <div className="card-icon">
                            ⏳
                        </div>

                        <span>
                            Pending KYC
                        </span>

                        <h2>
                            {dashboardData?.pendingKyc ?? 0}
                        </h2>

                    </div>


                    <div className="card approved-card">

                        <div className="card-icon">
                            ✓
                        </div>

                        <span>
                            Approved KYC
                        </span>

                        <h2>
                            {dashboardData?.approvedKyc ?? 0}
                        </h2>

                    </div>


                    <div className="card rejected-card">

                        <div className="card-icon">
                            ✕
                        </div>

                        <span>
                            Rejected KYC
                        </span>

                        <h2>
                            {dashboardData?.rejectedKyc ?? 0}
                        </h2>

                    </div>

                </div>

                {/* Risk + Compliance */}

                <div className="section-grid">

                    {/* Risk Overview */}

                    <div className="panel">

                        <h3>
                            Risk Overview
                        </h3>

                        <div className="risk-row low-risk">

                            <span>
                                Low Risk
                            </span>

                            <strong>
                                {dashboardData?.lowRisk ?? 0}
                            </strong>

                        </div>

                        <div className="risk-row medium-risk">

                            <span>
                                Medium Risk
                            </span>

                            <strong>
                                {dashboardData?.mediumRisk ?? 0}
                            </strong>

                        </div>

                        <div className="risk-row high-risk">

                            <span>
                                High Risk
                            </span>

                            <strong>
                                {dashboardData?.highRisk ?? 0}
                            </strong>

                        </div>

                    </div>


                    {/* Compliance Alerts */}

                    <div className="panel">

                        <h3>
                            Compliance Alerts
                        </h3>

                        <div className="alert-row pep-alert">

                            <div>

                                <span>
                                    PEP Matches
                                </span>

                                <small>
                                    Politically Exposed Persons
                                </small>

                            </div>

                            <strong>
                                {dashboardData?.pepMatches ?? 0}
                            </strong>

                        </div>


                        <div className="alert-row expiry-alert">

                            <div>

                                <span>
                                    Expired Documents
                                </span>

                                <small>
                                    Documents requiring attention
                                </small>

                            </div>

                            <strong>
                                {dashboardData?.expiredDocuments ?? 0}
                            </strong>

                        </div>

                    </div>

                </div>

                {/* Recent Activity */}

                <div className="activity-panel">

                    <h3>
                        Recent Activity
                    </h3>

                    <table className="activity-table">

                        <thead>

                            <tr>

                                <th>
                                    Action
                                </th>

                                <th>
                                    Description
                                </th>

                                <th>
                                    Date & Time
                                </th>

                            </tr>

                        </thead>

                        <tbody>

                            {auditLogs.length === 0 ? (

                                <tr>

                                    <td
                                        colSpan="3"
                                        style={{
                                            textAlign: "center",
                                            padding: "25px"
                                        }}
                                    >
                                        No recent activities
                                    </td>

                                </tr>

                            ) : (

                                auditLogs.slice(0, 5).map((log) => (

                                    <tr key={log.id}>

                                        <td>

                                            <span
                                                className={`action-badge ${
                                                    log.action === "KYC_APPROVED" ||
                                                    log.action === "KYC_VERIFIED"
                                                        ? "approved-badge"
                                                        : log.action === "KYC_REJECTED"
                                                        ? "rejected-badge"
                                                        : log.action === "PEP_SCREENING_COMPLETED"
                                                        ? "pep-badge"
                                                        : "default-badge"
                                                }`}
                                            >
                                                {log.action}
                                            </span>

                                        </td>

                                        <td>
                                            {log.description}
                                        </td>

                                        <td>

                                            {log.performedAt
                                                ? new Date(
                                                    log.performedAt
                                                ).toLocaleString()
                                                : "N/A"}

                                        </td>

                                    </tr>

                                ))

                            )}

                        </tbody>

                    </table>

                </div>

            </main>

        </div>
    );
}

export default Dashboard;