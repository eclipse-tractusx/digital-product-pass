export default {
    exists(key, json) {
        try {
            if (Object.prototype.hasOwnProperty.call(json, key)) {
                return true;
            }
        } catch {
            //Skip
        }
        return false;
    },
    copy(originObj) {
        let returnValue = null
        let err = false
        try {
            Object.assign(originObj, returnValue) // Copy object with JS
        } catch {
            err = true;
        }
        if (err || returnValue == null) {
            try {
                returnValue = JSON.parse(JSON.stringify(originObj)) // If can not copy with JS copy with JSON
            } catch {
                throw new Error('Failed to copy Json [' + returnValue.toString() + ']');
            }
        }
        return returnValue
    },
}
