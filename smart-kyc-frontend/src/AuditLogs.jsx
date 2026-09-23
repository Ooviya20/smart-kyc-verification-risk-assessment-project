import { useEffect, useState } from "react";
import "./Customers.css";

function AuditLogs({ setPage }) {

    const [auditLogs, setAuditLogs] = useState([]);

    useEffect(() => {

        fetch("http://localhost:8080/api/audit-logs")
            .then((response) => response.json())
            .then((data) => {
                setAuditLogs(data);
            })
            .catch((error) => {
                console.error("Error fetching audit logs:", error);
            });

    }, []);

    return (
        <div className="dashboard">

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

                    <a onClick={() => setPage("risk")}>
                        Risk Assessment
                    </a>

                    <a onClick={() => setPage("pep")}>
                        PEP Screening
                    </a>

                    <a className="active">
                        Audit Logs
                    </a>

                </nav>

            </aside>

            <main className="main-content">

                <div className="customers-page">

                    <div className="customers-header">

                        <div>

                            <h1>
                                Audit Logs
                            </h1>

                            <p>
                                Track KYC verification and system activities
                            </p>

                        </div>

                    </div>

                    <div className="customers-card">

                        <div className="table-header">

                            <h3>
                                Recent Activities
                            </h3>

                            <span>
                                {auditLogs.length} Activities
                            </span>

                        </div>

                        <div className="table-responsive">

                            <table className="customers-table">

                                <thead>

                                    <tr>
                                        <th>ID</th>
                                        <th>Action</th>
                                        <th>Description</th>
                                        <th>Date & Time</th>
                                    </tr>

                                </thead>

                                <tbody>

                                    {auditLogs.map((log) => (

                                        <tr key={log.id}>

                                            <td>
                                                #{log.id}
                                            </td>

                                            <td>

                                                <span
                                                    className={`verification-badge ${
    log.action === "KYC_APPROVED" || log.action === "KYC_VERIFIED"
        ? "approved"
        : log.action === "KYC_REJECTED" || log.action === "PEP_MATCH"
        ? "rejected"
        : log.action === "PEP_SCREENING_COMPLETED"
        ? "pending"
        : "pending"
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

export default AuditLogs;