const pool = require("../config/db");

exports.getHistory = async (req, res) => {
  const { user_id } = req.body;
  if (!user_id) return res.status(401).send("Id undefined");

  try {
    const userHistoryResult = await pool.query(
      "SELECT * FROM result_history WHERE user_id = $1",
      [user_id]
    );
    const userHistory = userHistoryResult.rows;

    if (!userHistory || userHistory.length === 0) {
      return res
        .status(404)
        .send("There's no history. Try analyze some images!");
    }

    res.status(200).json(userHistory);
  } catch (err) {
    console.error(err);
    res.status(500).send("Server error");
  }
};

exports.getHistoryById = async (req, res) => {
  const { user_id, scan_id } = req.body;
  if (!user_id || !scan_id) return res.status(401).send("Id undefined");

  try {
    const historyResult = await pool.query(
      "SELECT * FROM result_history WHERE user_id = $1 AND scan_id = $2",
      [user_id, scan_id]
    );

    const history = historyResult.rows;

    if (!history || history.length === 0) {
      return res.status(404).send("History not found");
    }
    res.status(200).json(history);
  } catch (err) {
    console.error(err);
    res.status(500).send("Server error");
  }
};
