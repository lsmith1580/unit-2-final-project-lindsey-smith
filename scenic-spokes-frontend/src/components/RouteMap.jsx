import { Link } from "react-router-dom";
import { useState, useEffect, useRef } from "react";
import Button from "./Button";
import { stops } from "../shared/stops";
import ConfirmModal from "./ConfirmModal";
import "./RouteMap.css";

const RouteMap = () => {
  const [packingList, setPackingList] = useState([]);
  const [newItem, setNewItem] = useState("");
  const [editingIndex, setEditingIndex] = useState(null); //variable to keep track of which item is being edited
  const [editingValue, setEditingValue] = useState("");
  const [showConfirm, setShowConfirm] = useState(false);
  const [itemToDeleteIndex, setItemToDeleteIndex] = useState(null);

  const isInitialMount = useRef(true);

  useEffect(() => {
    const storedList = localStorage.getItem("packingList");
    if (storedList) {
      setPackingList(JSON.parse(storedList));
    }
  }, []);

  useEffect(() => {
    if (isInitialMount.current) {
      //keeps this useEffect from running on initial mount and saving the empty dependency array back to local storage
      isInitialMount.current = false; //and overwriting any saved data
      return;
    }
    try {
      localStorage.setItem("packingList", JSON.stringify(packingList));
    } catch (e) {
      if (e.name === "QuotaExceededError") {
        console.error("LocalStorage quota exceeded.");
      }
    }
  }, [packingList]);

  const addItem = () => {
    if (newItem.trim() !== "") {
      setPackingList([...packingList, newItem]);
      setNewItem("");
    } else {
      return;
    }
  };

  const deleteItem = (index) => {
    const updatedList = packingList.filter((_, i) => i !== index);
    setPackingList(updatedList);
  };

  const handleConfirmDelete = () => {
    if (itemToDeleteIndex !== null) {
      deleteItem(itemToDeleteIndex);
      setShowConfirm(false);
      setItemToDeleteIndex(null);
    }
  };

  const handleCancelDelete = () => {
    setShowConfirm(false);
    setItemToDeleteIndex(null);
  };

  const startEditing = (index, value) => {
    setEditingIndex(index);
    setEditingValue(value);
  };

  const saveEdit = (index) => {
    const trimmedValue = editingValue.trim();
    if (trimmedValue === "") {
      setEditingIndex(null);
      setEditingValue("");
      return;
    }

    const updatedList = [...packingList];
    updatedList[index] = trimmedValue;
    setPackingList(updatedList);
    setEditingIndex(null);
    setEditingValue("");
  };

  return (
    <div className="route-map">
      <div className="sidebar">
        <table className="stops-table">
          <thead>
            <tr>
              <th>
                <h2>Stops</h2>
              </th>
            </tr>
          </thead>
          <tbody>
            {stops.map((stop) => (
              <tr key={stop.id}>
                <td>
                  <Link to={`/routes/${stop.id}`} className="details-link">
                    {stop.name}
                  </Link>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      <div className="packing-list-area">
        <h2>Packing List</h2>

        {packingList.length === 0 ? (
          <p>No items yet — add something!</p>
        ) : (
          <ul className="packing-list">
            {packingList.map((item, index) => (
              <li key={index}>
                {editingIndex === index ? (
                  <input
                    value={editingValue}
                    onChange={(e) => setEditingValue(e.target.value)}
                    onBlur={() => saveEdit(index)}
                    onKeyDown={(e) => {
                      if (e.key === "Enter") saveEdit(index);
                    }}
                    autoFocus
                  />
                ) : (
                  <>
                    {item}
                    <div>
                      <Button
                        variant="secondary"
                        onClick={() => startEditing(index, item)}
                      >
                        Edit
                      </Button>
                      <Button
                        variant="danger"
                        onClick={() => {
                          setItemToDeleteIndex(index);
                          setShowConfirm(true);
                        }}
                      >
                        Delete
                      </Button>
                    </div>
                  </>
                )}
              </li>
            ))}
          </ul>
        )}

        <div className="packing-input">
          <input
            type="text"
            placeholder="Add item..."
            value={newItem}
            onChange={(e) => setNewItem(e.target.value)}
          />
          <Button onClick={addItem}>Add</Button>
        </div>

        {/* Show confirmation modal only when triggered */}
        {showConfirm && (
          <ConfirmModal
            message="Are you sure you want to delete this item?"
            onConfirm={handleConfirmDelete}
            onCancel={handleCancelDelete}
          />
        )}
      </div>

      <div className="mobile-route-map-image"></div>
    </div>
  );
};

export default RouteMap;
