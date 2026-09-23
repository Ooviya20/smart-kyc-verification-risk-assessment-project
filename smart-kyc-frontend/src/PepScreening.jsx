import { useEffect, useState } from "react";
import "./Customers.css";

function PepScreening({ setPage }) {

    const [pepData, setPepData] = useState([]);

    useEffect(() => {

        fetch("http://localhost:8080/api/pep")
            .then((response) => response.json())
            .then((data) => {
                setPepData(data);
            })
            .catch((error) => {
                console.error("Error fetching PEP data:", error);
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

                    <a className="active">
                        PEP Screening
                    </a>

                    <a onClick={() => setPage("audit")}>
    Audit Logs
</a>

                </nav>

            </aside>

            <main className="main-content">

                <div className="customers-page">

                    <div className="customers-header">

                        <div>

                            <h1>
                                PEP Screening
                            </h1>

                            <p>
                                Monitor Politically Exposed Person screening results
                            </p>

                        </div>

                    </div>

                    <div className="risk-summary">

                        <div className="risk-card">

                            <span>
                                Total Screenings
                            </span>

                            <strong>
                                {pepData.length}
                            </strong>

                        </div>

                        <div className="risk-card">

                            <span>
                                PEP Matches
                            </span>

                            <strong>
                                {
                                    pepData.filter(
                                        pep => pep.pepStatus === true
                                    ).length
                                }
                            </strong>

                        </div>

                        <div className="risk-card">

                            <span>
                                Clear
                            </span>

                            <strong>
                                {
                                    pepData.filter(
                                        pep => pep.pepStatus === false
                                    ).length
                                }
                            </strong>

                        </div>

                    </div>

                    <div className="customers-card">

                        <div className="table-header">

                            <h3>
                                PEP Screening Results
                            </h3>

                            <span>
                                {pepData.length} Screenings
                            </span>

                        </div>

                        <div className="table-responsive">

                            <table className="customers-table">

                                <thead>

                                    <tr>

                                        <th>ID</th>
                                        <th>Customer Name</th>
                                        <th>PEP Status</th>
                                        <th>Screening Result</th>
                                        <th>Remarks</th>
                                        <th>Screened At</th>

                                    </tr>

                                </thead>

                                <tbody>

                                    {pepData.map((pep) => (

                                        <tr key={pep.id}>

                                            <td>
                                                #{pep.id}
                                            </td>

                                            <td>
                                                <strong>
                                                    {pep.customer?.fullName || "N/A"}
                                                </strong>
                                            </td>

                                            <td>

                                                <span
                                                    className={`risk-badge ${
                                                        pep.pepStatus
                                                            ? "high"
                                                            : "low"
                                                    }`}
                                                >
                                                    {pep.pepStatus
                                                        ? "MATCH"
                                                        : "CLEAR"}
                                                </span>

                                            </td>

                                            <td>
                                                {pep.screeningResult}
                                            </td>

                                            <td>
                                                <span className="risk-reason">
                                                    {pep.remarks || "No remarks"}
                                                </span>
                                            </td>

                                            <td>

                                                {pep.screenedAt
                                                    ? new Date(
                                                        pep.screenedAt
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

export default PepScreening;