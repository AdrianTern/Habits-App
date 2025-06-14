// Reducer to configure each settings state
export function settingsReducer(state, action){
    switch(action.type){
        case 'SET_DESC_VISIBILITY':
            return{ ...state, isShowTaskDesc: action.payload };
        case 'SET_DATE_VISIBILITY':
            return{ ...state, isShowTaskDate: action.payload };
        case 'SET_DARKMODE':
            return{ ...state, darkMode: action.payload };
        default:
            return state;
    }
}